// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by a BSD
// license that can be found in the LICENSE file
// at the root directory of this project.

package frc.robot.subsystems.vision;

import static frc.robot.subsystems.vision.VisionConstants.*;

import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Distance;
import static edu.wpi.first.units.Units.*;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.units.DistanceUnit;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.vision.VisionIO.PoseObservationType;
import java.util.LinkedList;
import java.util.List;
import org.littletonrobotics.junction.Logger;

public class Vision extends SubsystemBase {
  private final VisionConsumer consumer;
  private final VisionIO[] io;
  private final VisionIOInputsAutoLogged[] inputs;
  private final Alert[] disconnectedAlerts;
  private double latestDist;

  public Vision(VisionConsumer consumer, VisionIO... io) {
    this.consumer = consumer;
    this.io = io;

    // Initialize inputs
    this.inputs = new VisionIOInputsAutoLogged[io.length];
    for (int i = 0; i < inputs.length; i++) {
      inputs[i] = new VisionIOInputsAutoLogged();
    }

    // Initialize disconnected alerts
    this.disconnectedAlerts = new Alert[io.length];
    for (int i = 0; i < inputs.length; i++) {
      disconnectedAlerts[i] =
          new Alert(
              "Vision camera " + Integer.toString(i) + " is disconnected.", AlertType.kWarning);
    }
  }

  /**
   * Gets the distance to a specific AprilTag ID seen by a specific camera.
   *
   * @return Distance in meters, or -1.0 if the tag is not currently visible.
   */
  public double getDistanceToSpecificTag(int cameraIndex, int targetTagId) {
    if (cameraIndex < 0 || cameraIndex >= inputs.length) return -1.0;

    var targetObs = inputs[cameraIndex].latestTargetObservation;

    if (targetObs.id() == targetTagId) {
      double xd = targetObs.transform3d().getX();
      double yd = targetObs.transform3d().getY();
      double zd = targetObs.transform3d().getZ();

      System.out.println("XD: " + xd);
      System.out.println("YD: " + yd);
      System.out.println("ZD: " + zd); // add this, was probably 0 before

      return Math.sqrt((xd * xd) + (yd * yd) + (zd * zd)); // pure 3D norm
    }

    // Fallback
    for (var obs : inputs[cameraIndex].poseObservations) {
      if (obs.tagCount() > 0 && obs.averageTagDistance() > 0) {
        return obs.averageTagDistance();
      }
    }

    return -1.0;
  }
  
  
  
  public Transform3d getTrueTagPoses(int cameraIndex1, Transform3d fromCenter1, double theta1, int cameraIndex2, Transform3d fromCenter2, double theta2){
      var targetObs1 = inputs[cameraIndex1].latestTargetObservation;
      
      Transform3d targetObs1TRANSFORM = targetObs1.transform3d();
      
      Transform3d trueTargetObs1TRANSFORM = targetObs1TRANSFORM.plus(fromCenter1);
      
      Rotation3d rotation1 = new Rotation3d(0, 0, Math.toDegrees(theta1));
      Transform3d true1 = new Transform3d(trueTargetObs1TRANSFORM.getTranslation(), rotation1);
      
      var targetObs2 = inputs[cameraIndex2].latestTargetObservation;
      
      Transform3d targetObs2TRANSFORM = targetObs2.transform3d();
      
      Transform3d trueTargetObs2TRANSFORM = targetObs2TRANSFORM.plus(fromCenter2);
      
      Rotation3d rotation2 = new Rotation3d(0, 0, Math.toDegrees(theta2));
      Transform3d true2 = new Transform3d(trueTargetObs2TRANSFORM.getTranslation(), rotation2);
      
      return new Transform3d(
          Meters.of(true1.getX() + (true2.getX() - true1.getX()) * 0.5),
          Meters.of(true1.getY() + (true2.getY() - true1.getY()) * 0.5),
          Meters.of(true1.getY() + (true2.getY() - true1.getY()) * 0.5),
          new Rotation3d()
      );
      
  }

  public double getLatestDistanceToSpecifigTag(int cameraIndex, int targetTagId) {
    if (cameraIndex < 0 || cameraIndex >= inputs.length) {
      return latestDist;
    }

    var targetObs = inputs[cameraIndex].latestTargetObservation;

    if (targetObs.id() == targetTagId) {
      double xd = targetObs.transform3d().getX();
      double yd = targetObs.transform3d().getY();
      double zd = targetObs.transform3d().getZ();

      System.out.println("XD: " + xd);
      System.out.println("YD: " + yd);
      System.out.println("ZD: " + zd); // add this, was probably 0 before

      latestDist = Math.sqrt((xd * xd) + (yd * yd) + (zd * zd));
    }

    return latestDist;
  }

  public double getAngleToSpecificTag(int cameraIndex, int targetTagId) {
    // 1. Array access syntax fix: inputs[cameraIndex] (no dot before bracket)
    if (cameraIndex < 0 || cameraIndex >= inputs.length) return -1.0;

    var targetObs = inputs[cameraIndex].latestTargetObservation;

    // 2. Safety check: make sure targetObs isn't null before calling methods
    if (targetObs != null && targetObs.id() == targetTagId) {
      // You can pull the transform once to keep the code clean
      var transform = targetObs.transform3d();
      double xd = transform.getX();
      double yd = transform.getY();

      // 3. Use Math.atan2(y, x)
      // This is safer than y/x because it handles the 90-degree case (x=0)
      // and keeps the correct sign for all quadrants.
      double angleRadians = Math.atan2(yd, xd);

      // 4. Conversion: Math.atan2 returns Radians.
      // Most FRC gyro/drivetrain logic uses Degrees.
      return Math.toDegrees(angleRadians);
    }

    return -1.0;
  }

  // /**
  //  * Returns the X angle to the best target, which can be used for simple servoing with vision.
  //  *
  //  * @param cameraIndex The index of the camera to use.
  //  */
  public Rotation2d getTargetX(int cameraIndex) {
    return inputs[cameraIndex].latestTargetObservation.tx();
  }

  public boolean isThereTargets(int cameraIndex) {
    return (inputs[cameraIndex].poseObservations.length > 0);
  }

  public int getLatestTagId(int cameraIndex) {
    if (cameraIndex < 0 || cameraIndex >= inputs.length) return -1;
    int[] ids = inputs[cameraIndex].tagIds;
    return (ids != null && ids.length > 0) ? ids[0] : -1;
  }
  // private hood Hood;
  @Override
  public void periodic() {
    Logger.recordOutput("Vision ID 0:", getDistanceToSpecificTag(0, 10));
    Logger.recordOutput("Vision ID 1:", getDistanceToSpecificTag(1, 10));

    for (int i = 0; i < io.length; i++) {
      io[i].updateInputs(inputs[i]);
      Logger.processInputs("Vision/Camera" + Integer.toString(i), inputs[i]);
    }
    // System.out.println("Index 0: " + getDistanceToSpecificTag(0, 10));
    // System.out.println("Index 1: " + getDistanceToSpecificTag(1, 10));
    // System.out.println("Index 2: " + getDistanceToSpecificTag(2, 10));
    // System.out.println("Index 3: " + getDistanceToSpecificTag(3, 10));

    // System.out.println("Length of inputs: " + inputs.length);
    // Initialize logging values
    List<Pose3d> allTagPoses = new LinkedList<>();
    List<Pose3d> allRobotPoses = new LinkedList<>();
    List<Pose3d> allRobotPosesAccepted = new LinkedList<>();
    List<Pose3d> allRobotPosesRejected = new LinkedList<>();

    // Loop over cameras
    for (int cameraIndex = 0; cameraIndex < io.length; cameraIndex++) {
      // Update disconnected alert
      disconnectedAlerts[cameraIndex].set(!inputs[cameraIndex].connected);

      // Initialize logging values
      List<Pose3d> tagPoses = new LinkedList<>();
      List<Pose3d> robotPoses = new LinkedList<>();
      List<Pose3d> robotPosesAccepted = new LinkedList<>();
      List<Pose3d> robotPosesRejected = new LinkedList<>();

      // Add tag poses
      //  System.out.println("Observed tag: " + getLatestTagId(0));
      // Loop over pose observations
      for (var observation : inputs[cameraIndex].poseObservations) {
        // Check whether to reject pose
        boolean rejectPose =
            observation.tagCount() == 0 // Must have at least one tag
                || (observation.tagCount() == 1
                    && observation.ambiguity() > maxAmbiguity) // Cannot be high ambiguity
                || Math.abs(observation.pose().getZ())
                    > maxZError // Must have realistic Z coordinate

                // Must be within the field boundaries
                || observation.pose().getX() < 0.0
                || observation.pose().getX() > aprilTagLayout.getFieldLength()
                || observation.pose().getY() < 0.0
                || observation.pose().getY() > aprilTagLayout.getFieldWidth();

        // Add pose to log
        robotPoses.add(observation.pose());
        if (rejectPose) {
          robotPosesRejected.add(observation.pose());
        } else {
          robotPosesAccepted.add(observation.pose());
        }

        // Skip if rejected
        if (rejectPose) {
          continue;
        }

        // Calculate standard deviations
        double stdDevFactor =
            Math.pow(observation.averageTagDistance(), 2.0) / observation.tagCount();
        double linearStdDev = linearStdDevBaseline * stdDevFactor;
        double angularStdDev = angularStdDevBaseline * stdDevFactor;
        if (observation.type() == PoseObservationType.MEGATAG_2) {
          linearStdDev *= linearStdDevMegatag2Factor;
          angularStdDev *= angularStdDevMegatag2Factor;
        }
        if (cameraIndex < cameraStdDevFactors.length) {
          linearStdDev *= cameraStdDevFactors[cameraIndex];
          angularStdDev *= cameraStdDevFactors[cameraIndex];
        }

        // Send vision observation
        consumer.accept(
            observation.pose().toPose2d(),
            observation.timestamp(),
            VecBuilder.fill(linearStdDev, linearStdDev, angularStdDev));
      }

      // Log camera metadata
      Logger.recordOutput(
          "Vision/Camera" + Integer.toString(cameraIndex) + "/TagPoses",
          tagPoses.toArray(new Pose3d[0]));
      Logger.recordOutput(
          "Vision/Camera" + Integer.toString(cameraIndex) + "/RobotPoses",
          robotPoses.toArray(new Pose3d[0]));
      Logger.recordOutput(
          "Vision/Camera" + Integer.toString(cameraIndex) + "/RobotPosesAccepted",
          robotPosesAccepted.toArray(new Pose3d[0]));
      Logger.recordOutput(
          "Vision/Camera" + Integer.toString(cameraIndex) + "/RobotPosesRejected",
          robotPosesRejected.toArray(new Pose3d[0]));
      allTagPoses.addAll(tagPoses);
      allRobotPoses.addAll(robotPoses);
      allRobotPosesAccepted.addAll(robotPosesAccepted);
      allRobotPosesRejected.addAll(robotPosesRejected);
    }

    // Log summary data
    Logger.recordOutput("Vision/Summary/TagPoses", allTagPoses.toArray(new Pose3d[0]));
    Logger.recordOutput("Vision/Summary/RobotPoses", allRobotPoses.toArray(new Pose3d[0]));
    Logger.recordOutput(
        "Vision/Summary/RobotPosesAccepted", allRobotPosesAccepted.toArray(new Pose3d[0]));
    Logger.recordOutput(
        "Vision/Summary/RobotPosesRejected", allRobotPosesRejected.toArray(new Pose3d[0]));

    // System.out.println("Distance: " + getDistanceToSpecificTag(0, 3));
    // for (int i = 0; i < inputs[0].poseObservations.length; i++) {
    //   System.out.println(
    //       "Distance function " + i + " : " + inputs[0].poseObservations[i].averageTagDistance());
    // }

    // System.out.println(String.format("Latest tarObv: %s", inputs[0].latestTargetObservation));
  }

  @FunctionalInterface
  public static interface VisionConsumer {
    public void accept(
        Pose2d visionRobotPoseMeters,
        double timestampSeconds,
        Matrix<N3, N1> visionMeasurementStdDevs);
  }
}

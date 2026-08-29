// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by a BSD
// license that can be found in the LICENSE file
// at the root directory of this project.

package frc.robot.subsystems.vision;

import static edu.wpi.first.units.Units.*;
import static frc.robot.subsystems.vision.VisionConstants.*;

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
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.vision.VisionIO.PoseObservationType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.DoubleSupplier;
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

  public Transform3d getTrueTagPoses(
      int cameraIndex1,
      Transform3d fromCenter1,
      double theta1,
      int cameraIndex2,
      Transform3d fromCenter2,
      double theta2) {
    if (cameraIndex1 < 0
        || cameraIndex1 >= inputs.length
        || cameraIndex2 < 0
        || cameraIndex2 >= inputs.length) {
      return new Transform3d();
    }

    var targetObs1 = inputs[cameraIndex1].latestTargetObservation;

    Transform3d targetObs1TRANSFORM = targetObs1.transform3d();

    Transform3d trueTargetObs1TRANSFORM = fromCenter1.plus(targetObs1TRANSFORM);

    Rotation3d rotation1 = new Rotation3d(0, 0, theta1);
    Transform3d true1 = new Transform3d(trueTargetObs1TRANSFORM.getTranslation(), rotation1);

    var targetObs2 = inputs[cameraIndex2].latestTargetObservation;

    Transform3d targetObs2TRANSFORM = targetObs2.transform3d();

    Transform3d trueTargetObs2TRANSFORM = fromCenter2.plus(targetObs2TRANSFORM);

    Rotation3d rotation2 = new Rotation3d(0, 0, theta2);
    Transform3d true2 = new Transform3d(trueTargetObs2TRANSFORM.getTranslation(), rotation2);

    return new Transform3d(
        Meters.of(true1.getX() + (true2.getX() - true1.getX()) * 0.5),
        Meters.of(true1.getY() + (true2.getY() - true1.getY()) * 0.5),
        Meters.of(true1.getZ() + (true2.getZ() - true1.getZ()) * 0.5),
        new Rotation3d());
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

  public double getLatestDistanceToSpecificTagSet(
      int cameraIndex,
      int targetTagId1,
      int targetTagId2,
      int targetTadId3,
      int targetTagId4,
      int targetTagId5,
      int targetTagId6) {
    // 1. Bounds checking
    if (cameraIndex < 0 || cameraIndex >= inputs.length) {
      return latestDist; // Return a default or cached value
    }

    var targetObs = inputs[cameraIndex].latestTargetObservation;

    // 2. Check if the observation exists and matches any ID in our list
    if (targetObs != null) {
      if (targetObs.id() == targetTagId1
          || targetObs.id() == targetTagId2
          || targetObs.id() == targetTadId3
          || targetObs.id() == targetTagId4
          || targetObs.id() == targetTagId5
          || targetObs.id() == targetTagId6) {
        double x = targetObs.transform3d().getX();
        double y = targetObs.transform3d().getY();
        double z = targetObs.transform3d().getZ();

        // 3. 3D Distance Formula: d = sqrt(x² + y² + z²)

        double distance = Math.sqrt(x * x + y * y + z * z);

        // Update your cached value if needed
        this.latestDist = distance;
        return distance;
      }
    }

    return latestDist; // Return previous best if no match found
  }

  public double getAngleToSpecificTag(int cameraIndex, int targetTagId) {
    if (cameraIndex < 0 || cameraIndex >= inputs.length) return -1.0;

    var targetObs = inputs[cameraIndex].latestTargetObservation;
    if (targetObs != null && targetObs.id() == targetTagId) {
      var transform = targetObs.transform3d();
      double xd = transform.getX();
      double yd = transform.getY();
      return Math.toDegrees(Math.atan2(yd, xd));
    }

    return -1.0;
  }

  public double Transform3dtoAngle(Transform3d transform) {
    return Math.toDegrees(Math.atan2(transform.getY(), transform.getX()));
  }

  private double hi = 0;

  public double getLatestAngleToSpecificTag(int cameraIndex, int targetTagId) {
    if (cameraIndex < 0 || cameraIndex >= inputs.length) return hi;

    var targetObs = inputs[cameraIndex].latestTargetObservation;
    if (targetObs != null && targetObs.id() == targetTagId) {
      var transform = targetObs.transform3d();
      double xd = transform.getX();
      double yd = transform.getY();
      hi = Math.toDegrees(Math.atan2(yd, xd));
    }

    return hi;
  }

  /**
   * Calculates the angle to a tag using two cameras, accounting for their physical positions on the
   * robot via offsets. * @param cameraIndex1 Index of first camera
   *
   * @param offset1 Transform3d from Robot Center to Camera 1
   * @param cameraIndex2 Index of second camera
   * @param offset2 Transform3d from Robot Center to Camera 2
   * @param targetTagId The AprilTag ID to look for
   */
  public double getLatestAngleToSpecificTagMultCam(
      int cameraIndex1,
      Transform3d offset1,
      int cameraIndex2,
      Transform3d offset2,
      int targetTagId) {

    boolean camera1Valid = cameraIndex1 >= 0 && cameraIndex1 < inputs.length;
    boolean camera2Valid = cameraIndex2 >= 0 && cameraIndex2 < inputs.length;
    if (!camera1Valid && !camera2Valid) {
      return hi;
    }

    var obs1 = camera1Valid ? inputs[cameraIndex1].latestTargetObservation : null;
    var obs2 = camera2Valid ? inputs[cameraIndex2].latestTargetObservation : null;

    Translation3d targetInRobotFrame1 = null;
    Translation3d targetInRobotFrame2 = null;

    if (obs1 != null && obs1.id() == targetTagId) {
      targetInRobotFrame1 = offset1.plus(obs1.transform3d()).getTranslation();
    }

    if (obs2 != null && obs2.id() == targetTagId) {
      targetInRobotFrame2 = offset2.plus(obs2.transform3d()).getTranslation();
    }

    Translation3d finalTargetTranslation = null;
    if (targetInRobotFrame1 != null && targetInRobotFrame2 != null) {
      finalTargetTranslation = targetInRobotFrame1.plus(targetInRobotFrame2).div(2.0);
    } else if (targetInRobotFrame1 != null) {
      finalTargetTranslation = targetInRobotFrame1;
    } else if (targetInRobotFrame2 != null) {
      finalTargetTranslation = targetInRobotFrame2;
    }

    if (finalTargetTranslation != null) {
      hi = Math.toDegrees(Math.atan2(finalTargetTranslation.getY(), finalTargetTranslation.getX()));
    }

    return hi;
  }

  public double getLatestAngleToSpecificTagSet(
      int cameraIndex,
      int targetTagId1,
      int targetTagId2,
      int targetTagId3,
      int targetTagId4,
      int targetTagId5,
      int targetTagId6,
      DoubleSupplier offset) {
    // 1. Array access syntax fix: inputs[cameraIndex] (no dot before bracket)
    if (cameraIndex < 0 || cameraIndex >= inputs.length) return hi;

    var targetObs = inputs[cameraIndex].latestTargetObservation;

    // 2. Safety check: make sure targetObs isn't null before calling methods
    if (targetObs != null
        && (targetObs.id() == targetTagId1
            || targetObs.id() == targetTagId2
            || targetObs.id() == targetTagId3
            || targetObs.id() == targetTagId4
            || targetObs.id() == targetTagId5
            || targetObs.id() == targetTagId6)) {
      var transform = targetObs.transform3d();
      double xd = transform.getX();
      double yd = transform.getY();
      hi = Math.toDegrees(Math.atan2(yd, xd)) + offset.getAsDouble();
      return hi;
    }

    return hi;
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

<<<<<<< HEAD
  private static Transform3d robotToCameraa0 =
      new Transform3d(new Translation3d(0.2286, 0.0, 0.0952), new Rotation3d(0.0, 0, Math.PI));
  private static Transform3d robotToCameraa1 =
      new Transform3d(new Translation3d(-0.2032, 0.0, 0.0952), new Rotation3d(0.0, 0, 0.0));
=======
  private Transform3d izaak = Transform3d.kZero;

  public Transform3d getTranslation(
      int[] camIds, int tagTarget, Translation3d[] translations, Rotation3d[] rotations) {
    if (camIds.length != translations.length
        || camIds.length > inputs.length
        || translations.length != rotations.length) {
      return izaak;
    }

    List<Transform3d> translationList = new ArrayList<>();
    boolean nick = false;
    for (int i = 0; i < rotations.length; i++) {
      // transform the translations to make it correct
      for (int j = 0; j < inputs[camIds[i]].tagIds.length; j++) {
        if (inputs[camIds[i]].tagIds[j] == tagTarget
            && inputs[camIds[i]].latestTargetObservation.id() == tagTarget) {
          nick = true;
        }
      }

      Translation3d grant_and_tyler_and_dylan_and_diddy =
          inputs[camIds[i]].latestTargetObservation.transform3d().getTranslation();
      Rotation3d currentRot = inputs[camIds[i]].latestTargetObservation.transform3d().getRotation();

      if (nick) {
        grant_and_tyler_and_dylan_and_diddy =
            grant_and_tyler_and_dylan_and_diddy.plus(
                new Translation3d(
                    -translations[i].getX(), -translations[i].getY(), -translations[i].getZ()));

        currentRot =
            currentRot.plus(
                new Rotation3d(-rotations[i].getX(), -rotations[i].getY(), -rotations[i].getZ()));

        System.out.println(
            "Rotation: X = "
                + currentRot.getX()
                + " \nY = "
                + currentRot.getY()
                + " \nZ = "
                + currentRot.getZ());

        System.out.println(
            "Translation: Y = "
                + grant_and_tyler_and_dylan_and_diddy.getX()
                + " \nY = "
                + grant_and_tyler_and_dylan_and_diddy.getY()
                + " \nZ = "
                + grant_and_tyler_and_dylan_and_diddy.getZ());

        translationList.add(new Transform3d(grant_and_tyler_and_dylan_and_diddy, currentRot));
      }
    }

    for (int i = 0; i < translationList.size(); i++) {
      if (izaak.getTranslation().getX() == 0
          && izaak.getTranslation().getY() == 0
          && izaak.getTranslation().getY() == 0) {
        izaak = translationList.get(i);
      } else {
        izaak =
            new Transform3d(
                new Translation3d(
                    (izaak.getTranslation().getX() + translationList.get(i).getTranslation().getX())
                        / 2,
                    (izaak.getTranslation().getY() + translationList.get(i).getTranslation().getY())
                        / 2,
                    (izaak.getTranslation().getZ() + translationList.get(i).getTranslation().getZ())
                        / 2),
                new Rotation3d(
                    (izaak.getRotation().getX() + translationList.get(i).getRotation().getX()) / 2,
                    (izaak.getRotation().getY() + translationList.get(i).getRotation().getY()) / 2,
                    (izaak.getRotation().getZ() + translationList.get(i).getRotation().getZ())
                        / 2));
      }
    }

    return izaak;
  }
>>>>>>> 2018b5c42350672215a91dd0db332f7d14f6185a

  // private hood Hood;
  @Override
  public void periodic() {
    Logger.recordOutput("Vision ID 0:", getDistanceToSpecificTag(0, 10));
    if (inputs.length > 1) {
      Logger.recordOutput(
          "Vision BothCam ID 0+1:",
          getLatestAngleToSpecificTagMultCam(0, robotToCamera0, 1, robotToCamera1, 10));
    } else {
      Logger.recordOutput("Vision SingleCam ID 0:", getAngleToSpecificTag(0, 10));
    }

    Logger.recordOutput("Vision hi:", getAngleToSpecificTag(0, 3));
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

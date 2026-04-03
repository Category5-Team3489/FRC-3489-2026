package frc.robot.subsystems.hood;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.vision.Vision;
import java.util.function.DoubleSupplier;
import org.littletonrobotics.junction.Logger;

public class hood extends SubsystemBase {
  private static final double DEGREES_PER_ROTATION = 3.48;
  private static final double DEGREES_OFFSET = 63.035;
  public hoodIO io;
  public boolean isFerryMode = false;
  // public final double maxHoodPos = -9.342285;
  public final double maxHoodPos = -9.0;
  public final double minHoodPos = -0.05;
  private double defaultPosition = 0.0;
  private double CONSTANT = 1;
  private double targetPositionRotations = 0.0;
  private double targetAngleDegrees = DEGREES_OFFSET;

  public double positionToDeg(double pos) {
    return pos * CONSTANT;
  }

  public void periodic() {
    double currentPositionRotations = io.getPos();
    Logger.recordOutput("Hood/CurrentPositionRotations", currentPositionRotations);
    Logger.recordOutput("Hood/CurrentAngleDegrees", positionToDegrees(currentPositionRotations));
    Logger.recordOutput("Hood/TargetPositionRotations", targetPositionRotations);
    Logger.recordOutput("Hood/TargetAngleDegrees", targetAngleDegrees);
  }

  public double posToDeg(DoubleSupplier yeah) {
    return DEGREES_PER_ROTATION * yeah.getAsDouble() + DEGREES_OFFSET;
  }

  public double degToPos(DoubleSupplier no) {
    return (no.getAsDouble() - DEGREES_OFFSET) / DEGREES_PER_ROTATION;
  }

  private double positionToDegrees(double positionRotations) {
    return DEGREES_PER_ROTATION * positionRotations + DEGREES_OFFSET;
  }

  private double degreesToPosition(double degrees) {
    return (degrees - DEGREES_OFFSET) / DEGREES_PER_ROTATION;
  }

  private void setTrackedPosition(double positionRotations) {
    targetPositionRotations = positionRotations;
    targetAngleDegrees = positionToDegrees(positionRotations);
    io.setHoodPos(positionRotations);
  }

  private void setTrackedAngle(double degrees) {
    targetAngleDegrees = degrees;
    targetPositionRotations = degreesToPosition(degrees);
    io.setHoodAngleDegrees(degrees);
  }

  public hood(hoodIO givenIo) {
    io = givenIo;
  }

  public Command spinHood(DoubleSupplier speed) {
    return Commands.run(() -> io.turnHood(speed.getAsDouble()));
  }

  public Command setHoodPosCommand(DoubleSupplier pos) {
    return Commands.run(() -> setTrackedPosition(pos.getAsDouble()), this);
  }

  public void setHoodPosVoid(DoubleSupplier pos) {
    setTrackedPosition(pos.getAsDouble());
  }

  public Command setHoodAngle(DoubleSupplier degrees) {
    return Commands.run(() -> setTrackedAngle(degrees.getAsDouble()), this);
  }

  public Command setHoodAngleVision(Vision eyes, int CamIndex) {
    return Commands.run(
        () ->
            setTrackedAngle(
                // MathUtil.clamp(eyes.getDistanceToSpecificTag(CamIndex, 10), 0.0, 5.0)));
                -10.0),
        this);
  }

  public Command toggleHoodPosition() {
    return Commands.run(() -> io.toggleHoodPosition(), this);
  }

  public Command setFerryMode(boolean ferryMode) {
    this.isFerryMode = ferryMode;
    return Commands.run(
        () -> setTrackedPosition(ferryMode ? this.maxHoodPos : this.minHoodPos), this);
  }

  public boolean isFerryMode() {
    return this.isFerryMode;
  }

  public void setDefaultPosition(double defPos) {
    this.defaultPosition = defPos;
  }

  public Command setHoodDefaultPositionCommand(DoubleSupplier stick) {
    return Commands.run(() -> setTrackedPosition(this.defaultPosition - stick.getAsDouble()), this);
  }

  public void setHoodDefaultPositionVoid() {
    setTrackedPosition(this.defaultPosition);
  }
}

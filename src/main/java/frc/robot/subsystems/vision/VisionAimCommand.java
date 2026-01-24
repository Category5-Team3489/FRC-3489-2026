package frc.robot.subsystems.vision;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.Drive;

public class VisionAimCommand extends Command {
  private final Drive drive;
  private final Vision vision;
  private final int cameraIndex;

  public VisionAimCommand(Drive drive, Vision vision, int cameraIndex) {
    this.drive = drive;
    this.vision = vision;
    this.cameraIndex = cameraIndex;
    addRequirements(drive); // We don't necessarily 'require' vision, just read from it
  }

  @Override
  public void execute() {
    // Use the function from your Vision subsystem
    Rotation2d offset = vision.getTargetX(cameraIndex);
    if (vision.isThereTargets(cameraIndex)) {
      // Logic to turn the robot based on that offset
      drive.runVelocity(new ChassisSpeeds(0, 0, (Math.pow(offset.getRadians(), 3)) / 3));
    }
  }
}

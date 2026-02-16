// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by a BSD
// license that can be found in the LICENSE file
// at the root directory of this project.

package frc.robot;

import static frc.robot.subsystems.vision.VisionConstants.*;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.commands.DriveCommands;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.indexer.index;
import frc.robot.subsystems.indexer.indexIOSim;
import frc.robot.subsystems.indexer.indexIOTalonFX;
import frc.robot.subsystems.intake.intake;
import frc.robot.subsystems.intake.intakeIOSim;
import frc.robot.subsystems.intake.intakeIOTalonFX;
import frc.robot.subsystems.shooter.shooter;
import frc.robot.subsystems.shooter.shooterIOSim;
import frc.robot.subsystems.shooter.shooterIOTalonFX;
import frc.robot.subsystems.turrent.turrent;
import frc.robot.subsystems.turrent.turrentIOSim;
import frc.robot.subsystems.turrent.turrentIOTalonFX;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // Subsystems
  private final Drive drive;
  // private final Vision vision;
  private final intake Intake;
  private final shooter Shooter;
  private final turrent Turrent;

  //   private final climber Climber;
  private final index Index;
  // Controller
  private final CommandXboxController controller = new CommandXboxController(0);

  // Dashboard inputs
  private final LoggedDashboardChooser<Command> autoChooser;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    switch (Constants.currentMode) {
      case REAL:
        // Real robot, instantiate hardware IO implementations
        // ModuleIOTalonF
        Turrent = new turrent(new turrentIOTalonFX(15, 18));
        drive =
            new Drive(
                new GyroIOPigeon2(),
                new ModuleIOTalonFX(TunerConstants.FrontLeft),
                new ModuleIOTalonFX(TunerConstants.FrontRight),
                new ModuleIOTalonFX(TunerConstants.BackLeft),
                new ModuleIOTalonFX(TunerConstants.BackRight));
        // Real robot, instantiate hardware IO implementations
        // vision =
        //     new Vision(
        //         drive::addVisionMeasurement,
        //         new VisionIOPhotonVision(camera0Name, robotToCamera0),
        //         new VisionIOPhotonVision(camera1Name, robotToCamera1));

        // Turrent = new turrent(new turrentIOTalonFX(0));
        Index = new index(new indexIOTalonFX(14));
        Intake = new intake(new intakeIOTalonFX(22, 1, 1));

        Shooter = new shooter(0.4, new shooterIOTalonFX(17, 18, 15));

        // The ModuleIOTalonFXS implementation provides an example implementation for
        // TalonFXS controller connected to a CANdi with a PWM encoder. The
        // implementations
        // of ModuleIOTalonFX, ModuleIOTalonFXS, and ModuleIOSpark (from the Spark
        // swerve
        // template) can be freely intermixed to support alternative hardware
        // arrangements.
        // Please see the AdvantageKit template documentation for more information:
        // https://docs.advantagekit.org/getting-started/template-projects/talonfx-swerve-template#custom-module-implementations
        //
        // drive =
        // new Drive(
        // new GyroIOPigeon2(),
        // new ModuleIOTalonFXS(TunerConstants.FrontLeft),
        // new ModuleIOTalonFXS(TunerConstants.FrontRight),
        // new ModuleIOTalonFXS(TunerConstants.BackLeft),
        // new ModuleIOTalonFXS(TunerConstants.BackRight));
        break;

      case SIM:
        Turrent = new turrent(new turrentIOSim(1));
        Shooter = new shooter(0.4, new shooterIOSim());
        Intake = new intake(new intakeIOSim());
        Index = new index(new indexIOSim());
        // Turrent = new turrent(new turrentIOSim(1));
        // Sim robot, instantiate physics sim IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIOSim(TunerConstants.FrontLeft),
                new ModuleIOSim(TunerConstants.FrontRight),
                new ModuleIOSim(TunerConstants.BackLeft),
                new ModuleIOSim(TunerConstants.BackRight));
        // Sim robot, instantiate physics sim IO implementations
        // vision =
        //     new Vision(
        //         drive::addVisionMeasurement,
        //         new VisionIOPhotonVisionSim(camera0Name, robotToCamera0, drive::getPose),
        //         new VisionIOPhotonVisionSim(camera1Name, robotToCamera1, drive::getPose));

        break;

      default:
        Turrent = new turrent(new turrentIOTalonFX(15,18));
        // Turrent = new turrent(new turrentIOTalonFX(0));
        Shooter = new shooter(0.4, new shooterIOTalonFX(17, 18, 15));
        Intake = new intake(new intakeIOTalonFX(22, 1, 1));
        // Replayed robot, disable IO implementations
        Index = new index(new indexIOTalonFX(14));
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {});

        // (Use same number of dummy implementations as the real robot)
        // vision = new Vision(drive::addVisionMeasurement, new VisionIO() {}, new VisionIO() {});

        break;
    }

    // Set up auto routines
    autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());

    // Set up SysId routines
    autoChooser.addOption(
        "Drive Wheel Radius Characterization", DriveCommands.wheelRadiusCharacterization(drive));
    autoChooser.addOption(
        "Drive Simple FF Characterization", DriveCommands.feedforwardCharacterization(drive));
    autoChooser.addOption(
        "Drive SysId (Quasistatic Forward)",
        drive.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
    autoChooser.addOption(
        "Drive SysId (Quasistatic Reverse)",
        drive.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
    autoChooser.addOption(
        "Drive SysId (Dynamic Forward)", drive.sysIdDynamic(SysIdRoutine.Direction.kForward));
    autoChooser.addOption(
        "Drive SysId (Dynamic Reverse)", drive.sysIdDynamic(SysIdRoutine.Direction.kReverse));

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Default command, normal field-relative drive
    Turrent.setDefaultCommand(Turrent.turnTurrent(controller.getRightY()*0.2));

    drive.setDefaultCommand(
        DriveCommands.joystickDrive(
            drive,
            () -> -controller.getLeftY(),
            () -> -controller.getLeftX(),
            () -> -controller.getRightX()));
    Intake.setDefaultCommand(Intake.noSpin());
    // Lock to 0° when A button is held
    controller.rightTrigger().whileTrue(Intake.spinTheStuff(controller.getRightTriggerAxis()));
    // Default shooter command: run the shooter at a low speed (1 unit as
    // defined by shooter.shootAtSpeed). The returned command already
    // requires the `Shooter` subsystem.
    Shooter.setDefaultCommand(Shooter.shootAtSpeed(1));
    controller.leftBumper().whileTrue(Commands.run(() -> Index.spinMotor(0.5)));
    controller
        .a()
        .whileTrue(
            DriveCommands.joystickDriveAtAngle(
                drive,
                () -> -controller.getLeftY(),
                () -> -controller.getLeftX(),
                () -> Rotation2d.kZero));

    controller.y().whileTrue(Shooter.noShoot());
    // Switch to X pattern when X button is pressed
    controller.x().onTrue(Commands.runOnce(drive::stopWithX, drive));

    Index.setDefaultCommand(Commands.run(() -> Index.spinMotor(0), Index));
    // Change .leftTrigger to what you want it to be to half velocity.
    controller
        .leftTrigger()
        .whileTrue(
            DriveCommands.joystickDrive(
                drive,
                () -> -controller.getLeftY() * (1 - controller.getLeftTriggerAxis()),
                () -> -controller.getLeftX() * (1 - controller.getLeftTriggerAxis()),
                () -> -controller.getRightX() * (1 - controller.getLeftTriggerAxis())));

    // Reset gyro to 0 when B button is pressed
    controller
        .b()
        .onTrue(
            Commands.runOnce(
                    () ->
                        drive.setPose(
                            new Pose2d(drive.getPose().getTranslation(), Rotation2d.kZero)),
                    drive)
                .ignoringDisable(true));

    // controller
    //     .y()
    //     .whileTrue(
    //         Turrent.lockToTarget(
    //             vision,
    //             vision.getLatestTagId(0))); // Lock to target from camera 0 while Y button is
    // held
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.get();
  }
}

// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by a BSD
// license that can be found in the LICENSE file
// at the root directory of this project.

package frc.robot;

import static frc.robot.subsystems.vision.VisionConstants.*;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.commands.DriveCommands;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.climber.climber;
import frc.robot.subsystems.climber.climberIOSim;
import frc.robot.subsystems.climber.climberIOTalonFX;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.hood.hood;
import frc.robot.subsystems.hood.hoodIOTalonFX;
import frc.robot.subsystems.indexer.index;
import frc.robot.subsystems.indexer.indexIOSim;
import frc.robot.subsystems.indexer.indexIOTalonFX;
import frc.robot.subsystems.intake.intake;
import frc.robot.subsystems.intake.intakeIOSim;
import frc.robot.subsystems.intake.intakeIOTalonFX;
import frc.robot.subsystems.kicker.kicker;
import frc.robot.subsystems.kicker.kickerIOSim;
import frc.robot.subsystems.kicker.kickerIOTalonFX;
import frc.robot.subsystems.shooter.shooter;
import frc.robot.subsystems.shooter.shooterIOSim;
import frc.robot.subsystems.shooter.shooterIOTalonFX;
import frc.robot.subsystems.turrent.turrent;
import frc.robot.subsystems.turrent.turrentIOSim;
import frc.robot.subsystems.turrent.turrentIOTalonFX;
import frc.robot.subsystems.vision.Vision;
import frc.robot.subsystems.vision.VisionIOPhotonVision;
import frc.robot.subsystems.vision.VisionIOPhotonVisionSim;
import java.util.function.DoubleSupplier;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // Subsystems
  private final Timer manipRightTriggerTimer = new Timer();
  private final Drive drive;
  private final Vision vision;
  private final intake Intake;
  private final shooter Shooter;
  private final turrent Turrent;
  private final hood Hood;
  private final climber Climber;

  public double distToDeg(DoubleSupplier dist) {
    double tuff = (dist.getAsDouble() - 6.16) / (-0.068465);
    if (tuff > 70 || tuff < 35) {
      return 50;
    }
    return tuff;
  }

  public double calculateLaunchAngle(
      DoubleSupplier distanceMeters, DoubleSupplier heightMeters, double velocity) {
    double g = 9.81;
    double x = distanceMeters.getAsDouble();
    double y = heightMeters.getAsDouble();
    double v2 = velocity * velocity;
    double v4 = v2 * v2;

    double discriminant = v4 - g * (g * x * x + 2 * y * v2);

    if (discriminant < 0) {
      return 0; // Target out of range
    }

    // Using the '-' sign for a lower, flatter trajectory
    return Math.atan((v2 - Math.sqrt(discriminant)) / (g * x));
  }

  //   private final climber Climber;
  private final index Index;
  private final kicker Kicker;
  // Controller
  private final CommandXboxController controller = new CommandXboxController(0);
  private final CommandXboxController manipulatorController = new CommandXboxController(1);

  // Dashboard inputs
  private final LoggedDashboardChooser<Command> autoChooser;

  private Command shootWithIndexDelay(DoubleSupplier speed, double indexDelaySeconds) {
    return Commands.parallel(
        Shooter.shootAtSpeed(speed.getAsDouble()),
        Commands.runEnd(() -> Kicker.spinMotor(0.99), () -> Kicker.spinMotor(0.0), Kicker),
        Commands.runEnd(
            () -> {
              if (manipRightTriggerTimer.get() >= indexDelaySeconds) {
                Index.spinMotor(-0.99);
              } else {
                Index.spinMotor(0.0);
              }
            },
            () -> Index.spinMotor(0.0),
            Index));
  }

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    switch (Constants.currentMode) {
      case REAL:
        Climber = new climber(new climberIOTalonFX(31));
        Hood = new hood(new hoodIOTalonFX(18));
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
        vision =
            new Vision(
                drive::addVisionMeasurement,
                // new VisionIOPhotonVision(camera0Name, robotToCamera0),
                // new VisionIOPhotonVision(camera1Name, robotToCamera1),
                new VisionIOPhotonVision(camera2Name, robotToCamera2));

        // Turrent = new turrent(new turrentIOTalonFX(0));
        Index = new index(new indexIOTalonFX(16));
        Kicker = new kicker(new kickerIOTalonFX(14));
        Intake = new intake(new intakeIOTalonFX(22, 23, 24));

        Shooter = new shooter(0.4, new shooterIOTalonFX(20, 17));

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
        Climber = new climber(new climberIOSim());
        Hood = new hood(new hoodIOTalonFX(18));
        Turrent = new turrent(new turrentIOSim(1));
        Shooter = new shooter(0.4, new shooterIOSim());
        Intake = new intake(new intakeIOSim());
        Index = new index(new indexIOSim());
        Kicker = new kicker(new kickerIOSim());
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
        vision =
            new Vision(
                drive::addVisionMeasurement,
                // new VisionIOPhotonVisionSim(camera0Name, robotToCamera0, drive::getPose),
                // new VisionIOPhotonVisionSim(camera1Name, robotToCamera1, drive::getPose),
                new VisionIOPhotonVisionSim(camera2Name, robotToCamera2, drive::getPose));

        break;

      default:
        Climber = new climber(new climberIOTalonFX(31));
        Hood = new hood(new hoodIOTalonFX(18));
        Turrent = new turrent(new turrentIOTalonFX(15, 18));
        // Turrent = new turrent(new turrentIOTalonFX(0));
        Shooter = new shooter(0.4, new shooterIOTalonFX(20, 17));
        Intake = new intake(new intakeIOTalonFX(22, 1, 1));
        // Replayed robot, disable IO implementations
        Index = new index(new indexIOTalonFX(14));
        Kicker = new kicker(new kickerIOTalonFX(16));
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {});

        // (Use same number of dummy implementations as the real robot)
        vision =
            new Vision(
                drive::addVisionMeasurement,
                // new VisionIOPhotonVision(camera0Name, robotToCamera0),
                // new VisionIOPhotonVision(camera1Name, robotToCamera1),
                new VisionIOPhotonVision(camera2Name, robotToCamera2));

        break;
    }

    NamedCommands.registerCommand("intakeOn", Intake.spinTheStuff(0.4));

    NamedCommands.registerCommand("shooterOn", Shooter.shootAtSpeed(0.4));

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
    // Use a supplier so the joystick is sampled each scheduler cycle.
    Turrent.setDefaultCommand(Turrent.turnTurrent(() -> (-manipulatorController.getLeftX() * 0.2)));

    drive.setDefaultCommand(
        DriveCommands.joystickDrive(
            drive,
            () -> -controller.getLeftY(),
            () -> -controller.getLeftX(),
            () -> -controller.getRightX()));

    controller.povUp().whileTrue(DriveCommands.joystickDrive(drive, () -> 10, () -> 0, () -> 0));
    controller.povDown().whileTrue(DriveCommands.joystickDrive(drive, () -> -10, () -> 0, () -> 0));
    controller.povLeft().whileTrue(DriveCommands.joystickDrive(drive, () -> 0, () -> -10, () -> 0));
    controller.povRight().whileTrue(DriveCommands.joystickDrive(drive, () -> 0, () -> 10, () -> 0));
    Intake.setDefaultCommand(
        Intake.noSpin()
        .andThen(Intake.actuate(0.3))
        );
    // manipulatorController
    //     .leftTrigger(0.5)
    //     .whileTrue(
    //         Commands.parallel(
    //             Shooter.shootAtSpeed(0.4), Commands.run(() -> Kicker.spinMotor(0.99))));

    Hood.setDefaultCommand(
        Hood.setHoodPosCommand(() -> -Math.abs(manipulatorController.getRightY() * 10)));
    Climber.setDefaultCommand(Climber.moveClimbMotor(() -> manipulatorController.getLeftY() * -1));
    // Lock to 0° when A button is held
    // manipulatorController.y().onTrue(Commands.run(() -> Turrent.resetTurrentAngle()));

    var shootTrigger = manipulatorController.rightTrigger(0.1);
    shootTrigger.onTrue(
        Commands.runOnce(
            () -> {
              manipRightTriggerTimer.stop();
              manipRightTriggerTimer.reset();
              manipRightTriggerTimer.start();
            }));
    shootTrigger.onFalse(Commands.runOnce(() -> manipRightTriggerTimer.stop()));
    shootTrigger.whileTrue(shootWithIndexDelay(() -> 0.7, 1.0));
    // Default shooter command: map controller1 right trigger to shooter
    // voltage. Multiply axis [0..1] by 12 to convert to volts.

    // Shooter.setDefaultCommand(Shooter.shootAtSpeed(() -> controller1.getRightTriggerAxis() *
    // 0.7));
    // controller1
    //     .rightTrigger()
    //     .whileTrue(
    //         Commands.run(
    //             () -> Shooter.shootAtSpeed(() -> controller1.getRightTriggerAxis() * 0.7)));

    // manipulatorController.povUp().whileTrue(Intake.actuate(0.3));
    // manipulatorController.povDown().whileTrue(Intake.actuate(-0.3));
    // manipulatorController.povCenter().whileTrue(Intake.actuate(0));
    // manipulatorController.povLeft().onTrue(Shooter.nudgeHoodCalibration(-0.5));
    // manipulatorController.povRight().onTrue(Shooter.nudgeHoodCalibration(0.5));
    // manipulatorController.start().onTrue(Shooter.zeroHoodCalibration());

    double INITIAL_VELOCITY = 12.5;
    manipulatorController
        .a()
        .whileTrue(
            Hood.setHoodPosCommand(
                () ->
                    Hood.degToPos(() -> distToDeg(() -> vision.getDistanceToSpecificTag(0, 10)))));
    // () ->
    //     MathUtil.clamp(
    //         Hood.degToPos(
    //             () ->
    //                 Math.toDegrees(
    //                         calculateLaunchAngle(
    //                             () -> vision.getDistanceToSpecificTag(0, 3),
    //                             () -> 200,
    //                             INITIAL_VELOCITY))
    //                     * -1),
    //         -10,
    //         0)));
    // manipulatorController
    //     .leftBumper()
    //     .whileTrue(Commands.parallel(Intake.spinTheStuff(0.9), Intake.actuate(-0.6)));
    // manipulatorController.rightBumper().whileTrue(Commands.run(() -> Index.spinMotor(0.99)));
    // manipulatorController.x().onTrue(Commands.runOnce(() -> Turrent.resetTurrentAngle()));
    // right trigger binding handled above (with timing)
    // manipulatorController
    //     .b()
    //     .whileTrue(Intake.spinTheStuff(0.8));
    controller
        .a()
        .whileTrue(
            DriveCommands.joystickDriveAtAngle(
                drive,
                () -> -controller.getLeftY(),
                () -> -controller.getLeftX(),
                () -> Rotation2d.kZero));

    manipulatorController.x().whileTrue(Intake.spinTheStuff(-0.55));
    manipulatorController.b().whileTrue(Intake.spinTheStuff(0.8));
    // controller.y().whileTrue(Shooter.noShoot());
    // Switch to X pattern when X button is pressed
    controller.x().onTrue(Commands.runOnce(drive::stopWithX, drive));

    Index.setDefaultCommand(Commands.run(() -> Index.spinMotor(0), Index));
    Kicker.setDefaultCommand(Commands.run(() -> Kicker.spinMotor(0), Kicker));
    Shooter.setDefaultCommand(Shooter.shootAtSpeed(0.1));
    // Change .leftTrigger to what you want it to be to half vel]=ocity.
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

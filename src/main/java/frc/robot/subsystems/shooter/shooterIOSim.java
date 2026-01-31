// package frc.robot.subsystems.shooter;

// import edu.wpi.first.math.MathUtil;
// import edu.wpi.first.math.controller.PIDController;
// import edu.wpi.first.math.system.plant.DCMotor;
// import edu.wpi.first.math.system.plant.LinearSystemId;
// import edu.wpi.first.wpilibj.simulation.DCMotorSim;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.Commands;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;

// public class shooterIOSim extends SubsystemBase {

//   // Physical constants / configuration
//   private final double distanceFromGround;
//   private final double HubHeight = 1.829;

//   // Motor models (plant descriptions)
//   private final DCMotor angleMotorModel;
//   private final DCMotor shootMotorModel;

//   // Simulation objects
//   private final DCMotorSim angleSim;
//   private final DCMotorSim shootSim;

//   // Simple controllers and applied voltages
//   private final PIDController angleController = new PIDController(6.0, 0.0, 0.0);
//   private boolean angleClosedLoop = false;
//   private double angleAppliedVolts = 0.0;

//   private boolean shootClosedLoop = false;
//   private double shootAppliedVolts = 0.0;
//   private double shootSetpointRadPerSec = 0.0;

//   // Assumed inertias for simple sim models (reasonable defaults). If you have
//   // specific inertias use those instead.
//   private static final double ANGLE_INERTIA = 0.0005;
//   private static final double SHOOT_INERTIA = 0.001;

//   // When called, creates motor models with reductions
//   public shooterIOSim(
//       double distanceFromG, double angleMotorReduction, double shootMotorReduction) {
//     this.distanceFromGround = distanceFromG;

//     // Create DCMotor models and sims using provided reductions.
//     angleMotorModel = DCMotor.getKrakenX60Foc(1).withReduction(angleMotorReduction);
//     shootMotorModel = DCMotor.getKrakenX60Foc(1).withReduction(shootMotorReduction);

//     // Create LinearSystem-based motor sims using simple assumed inertias.
//     angleSim =
//         new DCMotorSim(
//             LinearSystemId.createDCMotorSystem(angleMotorModel, ANGLE_INERTIA, angleMotorReduction),
//             angleMotorModel);
//     shootSim =
//         new DCMotorSim(
//             LinearSystemId.createDCMotorSystem(shootMotorModel, SHOOT_INERTIA, shootMotorReduction),
//             shootMotorModel);
//   }

//   @Override
//   public void periodic() {
//     // Run closed-loop control if enabled
//     if (angleClosedLoop) {
//       // angle controller uses radians: controller expects measurement first, setpoint already set
//       double volts = angleController.calculate(angleSim.getAngularPositionRad());
//       angleAppliedVolts = MathUtil.clamp(volts, -12.0, 12.0);
//     }

//     if (shootClosedLoop) {
//       // simple proportional feedthrough to reach rotational velocity setpoint
//       double velMeas = shootSim.getAngularVelocityRadPerSec();
//       // P-only controller to reach velocity setpoint
//       double kP = 0.01; // small gain; tune as needed
//       double volts = kP * (shootSetpointRadPerSec - velMeas);
//       shootAppliedVolts = MathUtil.clamp(volts + 0.0, -12.0, 12.0);
//     }

//     // Apply voltages to sims and step
//     angleSim.setInputVoltage(MathUtil.clamp(angleAppliedVolts, -12.0, 12.0));
//     shootSim.setInputVoltage(MathUtil.clamp(shootAppliedVolts, -12.0, 12.0));
//     // typical robot periodic timestep
//     double dt = 0.02;
//     angleSim.update(dt);
//     shootSim.update(dt);
//   }

//   // Set angle (degrees) using closed-loop setpoint
//   public void moveToAngle(double degrees) {
//     double radians = Math.toRadians(degrees);
//     angleController.setSetpoint(radians);
//     angleClosedLoop = true;
//   }

//   // Command to spin shooter to a voltage determined from distance
//   public Command shootAtDistance(double distance) {
//     double volts = MathUtil.clamp(distance * 0.3 + 9.0, -12.0, 12.0);
//     return Commands.run(() -> setShootVoltage(volts));
//   }

//   // Command to move the top motor at a certain percent speed (-1..1). This maps
//   // percent to voltage and runs open-loop on the angle motor.
//   public Command moveTopMotor(double speed) {
//     return Commands.run(() -> setAngleOpenLoop(MathUtil.clamp(speed * 12.0, -12.0, 12.0)));
//   }

//   // Set the shooter wheel target rotational speed in rad/s (closed-loop)
//   public void setShootVelocityRadPerSec(double radPerSec) {
//     this.shootSetpointRadPerSec = radPerSec;
//     this.shootClosedLoop = true;
//   }

//   // Directly set applied voltage to shooter (open-loop)
//   public void setShootVoltage(double volts) {
//     this.shootAppliedVolts = MathUtil.clamp(volts, -12.0, 12.0);
//     this.shootClosedLoop = false;
//   }

//   // Directly set applied voltage to angle motor (open-loop)
//   public void setAngleOpenLoop(double volts) {
//     this.angleAppliedVolts = MathUtil.clamp(volts, -12.0, 12.0);
//     this.angleClosedLoop = false;
//   }

//   public double getNeededAngle(double distance, double initialSpeed, boolean PlusorMinus) {
//     // True is plus, false is minus.
//     if (PlusorMinus == true) {
//       return Math.toDegrees(
//           Math.atan(
//               (Math.pow(initialSpeed, 2)
//                       + Math.sqrt(
//                           Math.pow(initialSpeed, 4)
//                               - 9.8
//                                   * (9.8 * Math.pow(distance, 2)
//                                       + 2
//                                           * (HubHeight - distanceFromGround)
//                                           * Math.pow(initialSpeed, 2))))
//                   / (9.8 * distance)));
//     } else {
//       return Math.toDegrees(
//           Math.atan(
//               (Math.pow(initialSpeed, 2)
//                       - Math.sqrt(
//                           Math.pow(initialSpeed, 4)
//                               - 9.8
//                                   * (9.8 * Math.pow(distance, 2)
//                                       + 2
//                                           * (HubHeight - distanceFromGround)
//                                           * Math.pow(initialSpeed, 2))))
//                   / (9.8 * distance)));
//     }
//   }
// }

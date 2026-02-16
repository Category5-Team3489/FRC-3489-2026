package frc.robot.subsystems.shooter;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;

public class shooter extends SubsystemBase {

  private final double distanceFromGround;
  private final double HubHeight = 1.829;
  private shooterIO io;
  // private shooterIOInputsAutoLogged inputs = new shooterIOInputsAutoLogged();
  // When called, asks for motor's port and then  creates a motor with the port
  public shooter(double distanceFromG, shooterIO shooterIOIo) {
    this.distanceFromGround = distanceFromG;
    this.io = shooterIOIo;
  }

  // public void periodic() {
  //   // This method will be called once per scheduler run
  //   // io.updateInputs(inputs);
  // }
  // Yo mentor anthony, if you can see this I was wondering
  // If you could check over this function. If its wrong,
  // please tell me why
  public void moveToAngle(double degrees) {
    io.setShootAngle(degrees);
  }

  public Command noShoot() {
    return Commands.runOnce(() -> io.stopMotors(), this);
  }

  // Returns a Command that runs the shooter at the requested speed while
  // scheduled. The command requires this subsystem so it can be used as a
  // default command.
  public Command shootAtSpeed(double speed) {
    return Commands.run(() -> io.shootBall(speed), this);
  }

  // Overload that accepts a DoubleSupplier so the controller axis can be
  // sampled every scheduler run. The supplier should return volts (0..12)
  // if your IO implementation expects voltage; multiply the axis by 12 in
  // the supplier if passing trigger axis [0..1].
  public Command shootAtSpeed(DoubleSupplier speedSupplier) {
    return Commands.run(() -> io.shootBall(speedSupplier.getAsDouble()), this);
  }

  public double getNeededAngle(double distance, double initialSpeed, boolean PlusorMinus) {
    // True is plus, false is minus.
    if (PlusorMinus == true) {
      return Math.toDegrees(
          Math.atan(
              (Math.pow(initialSpeed, 2)
                      + Math.sqrt(
                          Math.pow(initialSpeed, 4)
                              - 9.8
                                  * (9.8 * Math.pow(distance, 2)
                                      + 2
                                          * (HubHeight - distanceFromGround)
                                          * Math.pow(initialSpeed, 2))))
                  / (9.8 * distance)));
    } else {
      return Math.toDegrees(
          Math.atan(
              (Math.pow(initialSpeed, 2)
                      - Math.sqrt(
                          Math.pow(initialSpeed, 4)
                              - 9.8
                                  * (9.8 * Math.pow(distance, 2)
                                      + 2
                                          * (HubHeight - distanceFromGround)
                                          * Math.pow(initialSpeed, 2))))
                  / (9.8 * distance)));
    }
  }
}

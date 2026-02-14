package frc.robot.subsystems.shooter;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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

  public void shootAtSpeed(double speed) {
    io.shootBall(speed);
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

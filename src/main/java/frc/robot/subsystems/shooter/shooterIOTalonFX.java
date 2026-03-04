package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;

public class shooterIOTalonFX implements shooterIO {
  // Create motors
  private final TalonFX angleMotor;
  private final TalonFX shooterMotor;
  private final TalonFX shootMotorOther; // Example of a second motor if needed

  private final shooterIOInputs inputs = new shooterIOInputs();
  // Local dashboard visualization (do not include in AutoLog inputs)
  private final Mechanism2d turnMechanism = new Mechanism2d(1, 1);
  private final MechanismRoot2d root = turnMechanism.getRoot("shooter root", 0, 0);
  private final MechanismLigament2d shooterTurn =
      root.append(new MechanismLigament2d("shooter direction", 1, 0));

  public shooterIOTalonFX(int shooterMotorPort, int angleMotorPort, int angleMotorPortOther) {
    angleMotor = new TalonFX(angleMotorPort);
    shooterMotor = new TalonFX(shooterMotorPort);
    shootMotorOther = new TalonFX(angleMotorPortOther); // Example of initializing a second motor
  }

  @Override
  public void updateInputs(shooterIOInputs inputs) {
    // TODO Auto-generated method stub
    // Phoenix6 StatusSignal APIs return signal objects; read numeric values
    inputs.topMotorCurrent = shooterMotor.getSupplyCurrent().getValueAsDouble();
    inputs.shootAngle =
        angleMotor.getPosition().getValueAsDouble()
            * 360.0
            * inputs.gearRatio; // Convert rotations to degrees, accounting for gear ratio
    inputs.bottomMotorCurrent = angleMotor.getSupplyCurrent().getValueAsDouble();
    inputs.distanceToTarget = 0.0; // This would need a sensor to be implemented
    // Update local visualization ligament
    shooterTurn.setAngle(inputs.shootAngle);
    // System.out.println(
    //     "Angle: "
    //         + inputs.shootAngle
    //         + " degrees, Shooter Current: "
    //         + inputs.topMotorCurrent
    //         + " A, Angle Motor Current: "
    //         + inputs.bottomMotorCurrent
    //         + " A");
  }

  @Override
  public void stopMotors() {
    // TODO Auto-generated method stub
    shooterMotor.set(0);
    angleMotor.set(0);
    shootMotorOther.set(0);
  }

  @Override
  public void shootBall(double speed) {
    // Check this code fs
    shooterMotor.set(-speed);
    angleMotor.set(0);
    shootMotorOther.set(speed);
  }

  @Override
  public void setShooterVoltage(double volts) {
    shooterMotor.setVoltage(volts);
  }

  @Override
  public void setShootAngle(double degrees) {
    // Check this code fs
    double rotations =
        (degrees / 360.0)
            * inputs.gearRatio; // Convert degrees to rotations, accounting for gear ratio
    PositionDutyCycle request = new PositionDutyCycle(rotations);
    angleMotor.setControl(request);
  }

  @Override
  public void setHoodSpeed(double speed) {
    angleMotor.set(speed);
  }

  public String simplify(String input) {
    double value = new ExpressionParser(input).parse();
    if (Math.abs(value - Math.rint(value)) < 1e-9) {
      return Long.toString((long) Math.rint(value));
    }
    return Double.toString(value);
  }

  private static class ExpressionParser {
    private final String expression;
    private int index = 0;

    ExpressionParser(String expression) {
      this.expression = expression.replaceAll("\\s+", "");
    }

    double parse() {
      double value = parseExpression();
      if (index != expression.length()) {
        throw new IllegalArgumentException("Unexpected token at index " + index);
      }
      return value;
    }

    private double parseExpression() {
      double value = parseTerm();
      while (index < expression.length()) {
        char operator = expression.charAt(index);
        if (operator != '+' && operator != '-') {
          break;
        }
        index++;
        double right = parseTerm();
        value = operator == '+' ? value + right : value - right;
      }
      return value;
    }

    private double parseTerm() {
      double value = parsePower();
      while (index < expression.length()) {
        char operator = expression.charAt(index);
        if (operator != '*' && operator != '/') {
          break;
        }
        index++;
        double right = parsePower();
        value = operator == '*' ? value * right : value / right;
      }
      return value;
    }

    private double parsePower() {
      double base = parseFactor();
      if (index < expression.length() && expression.charAt(index) == '^') {
        index++;
        double exponent = parsePower();
        return Math.pow(base, exponent);
      }
      return base;
    }

    private double parseFactor() {
      if (index >= expression.length()) {
        throw new IllegalArgumentException("Unexpected end of expression");
      }

      char current = expression.charAt(index);
      if (current == '+') {
        index++;
        return parseFactor();
      }
      if (current == '-') {
        index++;
        return -parseFactor();
      }
      if (current == '(') {
        index++;
        double value = parseExpression();
        if (index >= expression.length() || expression.charAt(index) != ')') {
          throw new IllegalArgumentException("Missing ')' at index " + index);
        }
        index++;
        return value;
      }

      int start = index;
      while (index < expression.length()) {
        char c = expression.charAt(index);
        if (!Character.isDigit(c) && c != '.') {
          break;
        }
        index++;
      }
      if (start == index) {
        throw new IllegalArgumentException("Expected number at index " + index);
      }
      return Double.parseDouble(expression.substring(start, index));
    }
  }
}

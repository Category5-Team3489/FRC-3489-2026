package frc.robot.subsystems.hood;

public interface hoodIO {

  public void turnHood(double speed);

  public void setHoodPos(double pos);

  public void setHoodAngleDegrees(double degrees);

  public double getDegrees();

  public void toggleHoodPosition();
}

// package frc.robot.subsystems.vision.VisionSIm;

// import edu.wpi.first.math.geometry.Rotation3d;
// import edu.wpi.first.math.geometry.Transform3d;
// import edu.wpi.first.math.geometry.Translation3d;
// import frc.robot.commands.*;
// import frc.robot.subsystems.vision.Vision;
// import frc.robot.subsystems.vision.VisionIO.VisionIOInputs;
// import java.util.ArrayList;
// import java.util.List;

// public class visionsim {
//   public Transform3d camTransform;
//   public double transformAngle;
//   public Rotation3d measuredAngle;

//   public String getInfo(int getXdir, int getYdir, Vision given) {
//     if (given.isThereTargets(0)) {
//       return "No Targets :/";
//     } else {
//       return "Test";
//     }
//   }

//   public Transform3d getTranslation(
//       int[] camIds,
//       int tagTarget,
//       Translation3d[] translations,
//       Rotation3d[] rotations,
//       VisionIOInputs[] inputs) {
//     if (camIds.length != translations.length
//         || camIds.length > inputs.length
//         || translations.length != rotations.length) {
//       return Transform3d.kZero;
//     }

//     Transform3d izaak = Transform3d.kZero;
//     List<Transform3d> translationList = new ArrayList<>();
//     boolean nick = false;
//     for (int i = 0; i < rotations.length; i++) {
//       // transform the translations to make it correct
//       for (int j = 0; j < inputs[camIds[i]].tagIds.length; j++) {
//         if (inputs[camIds[i]].tagIds[j] == tagTarget
//             && inputs[camIds[i]].latestTargetObservation.id() == tagTarget) {
//           nick = true;
//         }
//       }

//       Translation3d grant_and_tyler_and_dylan_and_diddy =
//           inputs[camIds[i]].latestTargetObservation.transform3d().getTranslation();
//       Rotation3d currentRot =
// inputs[camIds[i]].latestTargetObservation.transform3d().getRotation();

//       if (nick) {
//         grant_and_tyler_and_dylan_and_diddy =
//             grant_and_tyler_and_dylan_and_diddy.plus(
//                 new Translation3d(
//                     -translations[i].getX(), -translations[i].getY(), -translations[i].getZ()));

//         currentRot =
//             currentRot.plus(
//                 new Rotation3d(-rotations[i].getX(), -rotations[i].getY(),
// -rotations[i].getZ()));

//         System.out.println(
//             "Rotation: X = "
//                 + currentRot.getX()
//                 + " \nY = "
//                 + currentRot.getY()
//                 + " \nZ = "
//                 + currentRot.getZ());

//         System.out.println(
//             "Translation: Y = "
//                 + grant_and_tyler_and_dylan_and_diddy.getX()
//                 + " \nY = "
//                 + grant_and_tyler_and_dylan_and_diddy.getY()
//                 + " \nZ = "
//                 + grant_and_tyler_and_dylan_and_diddy.getZ());

//         translationList.add(new Transform3d(grant_and_tyler_and_dylan_and_diddy, currentRot));
//       }
//     }

//     for (int i = 0; i < translationList.size(); i++) {
//       if (izaak.getTranslation().getX() == 0
//           && izaak.getTranslation().getY() == 0
//           && izaak.getTranslation().getY() == 0) {
//         izaak = translationList.get(i);
//       } else {
//         izaak =
//             new Transform3d(
//                 new Translation3d(
//                     (izaak.getTranslation().getX() +
// translationList.get(i).getTranslation().getX())
//                         / 2,
//                     (izaak.getTranslation().getY() +
// translationList.get(i).getTranslation().getY())
//                         / 2,
//                     (izaak.getTranslation().getZ() +
// translationList.get(i).getTranslation().getZ())
//                         / 2),
//                 new Rotation3d(
//                     (izaak.getRotation().getX() + translationList.get(i).getRotation().getX()) /
// 2,
//                     (izaak.getRotation().getY() + translationList.get(i).getRotation().getY()) /
// 2,
//                     (izaak.getRotation().getZ() + translationList.get(i).getRotation().getZ())
//                         / 2));
//       }
//     }

//     return izaak;
//   }
// }

package eugen.enterprise.easyfit.model.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import eugen.enterprise.easyfit.acquaintance.enums.EExerciseType;
import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;
import eugen.enterprise.easyfit.acquaintance.enums.EMuscleGroup;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutDuration;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutExtras;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutSplit;
import eugen.enterprise.easyfit.model.DatabaseAccess;
import eugen.enterprise.easyfit.model.data_objects.Exercise;
import eugen.enterprise.easyfit.model.data_objects.ExerciseDao;
import eugen.enterprise.easyfit.model.data_objects.MuscleGroup;
import eugen.enterprise.easyfit.model.data_objects.MuscleGroupDao;

public class WorkoutGenerationService {

    public void createWorkout(EWorkoutSplit workoutSplit, EWorkoutDuration workoutDuration, List<EMuscleGroup> muscleGroups, EWorkoutExtras workoutExtras, Callback callback, Context c) {
        Runnable runnable = () -> {
            VerifyDatabaseData(c);

            List<MuscleGroup> muscleGroupsWithExercises = new ArrayList<>();

            for (EMuscleGroup eMuscleGroup : muscleGroups) {
                muscleGroupsWithExercises.add(getMuscleGroupWithExercises(c, eMuscleGroup));
            }

            generateWorkout(workoutSplit, workoutDuration, muscleGroupsWithExercises, workoutExtras);

            callback.onResponse("test");
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private MuscleGroup getMuscleGroupWithExercises(Context c, EMuscleGroup eMuscleGroup) {
        MuscleGroupDao muscleGroupDao = DatabaseAccess.getInstance().getDatabase(c).muscleGroupDao();
        MuscleGroup muscleGroup = muscleGroupDao.getMuscleGroup(eMuscleGroup.name());

        ExerciseDao exerciseDao = DatabaseAccess.getInstance().getDatabase(c).exerciseDao();
        Exercise[] exercises = exerciseDao.getMuscleGroupExercises(eMuscleGroup.name());

        List<Exercise> exerciseList = new ArrayList<>();
        for (Exercise exercise : exercises) {
            exerciseList.add(exercise);
        }
        muscleGroup.setExercises(exerciseList);

        return muscleGroup;
    }

    private void generateWorkout(EWorkoutSplit workoutSplit, EWorkoutDuration workoutDuration, List<MuscleGroup> muscleGroupsWithExercises, EWorkoutExtras workoutExtras) {
        int setsPrMuscleGroup = 0;
        int avgSecondsPrSet = 0;
        int numberOfExercises = 0;

        if (workoutDuration == EWorkoutDuration.OneHour) {
            if (workoutSplit == EWorkoutSplit.FullBody) {
                setsPrMuscleGroup = 4;
                avgSecondsPrSet = 150;
                numberOfExercises = ThreadLocalRandom.current().nextInt(1,2);
            } else if (workoutSplit == EWorkoutSplit.TwoSplit) {
                setsPrMuscleGroup = 6;
                avgSecondsPrSet = 180;
                numberOfExercises = ThreadLocalRandom.current().nextInt(2,3);
            } else if (workoutSplit == EWorkoutSplit.ThreeSplit) {
                setsPrMuscleGroup = 9;
                avgSecondsPrSet = 180;
                numberOfExercises = 3;
            }
        } else if (workoutDuration == EWorkoutDuration.OneHalfHour) {
            if (workoutSplit == EWorkoutSplit.FullBody) {
                setsPrMuscleGroup = 6;
                avgSecondsPrSet = 150;
                numberOfExercises = ThreadLocalRandom.current().nextInt(2, 3);
            } else if (workoutSplit == EWorkoutSplit.TwoSplit) {
                setsPrMuscleGroup = 9;
                avgSecondsPrSet = 210;
                numberOfExercises = 3;
            } else if (workoutSplit == EWorkoutSplit.ThreeSplit) {
                setsPrMuscleGroup = 12;
                avgSecondsPrSet = 210;
                numberOfExercises = ThreadLocalRandom.current().nextInt(3,4);
            }
        } else if (workoutDuration == EWorkoutDuration.TwoHours) {
            if (workoutSplit == EWorkoutSplit.FullBody) {
                setsPrMuscleGroup = 6;
                avgSecondsPrSet = 180;
                numberOfExercises = ThreadLocalRandom.current().nextInt(2,3);
            } else if (workoutSplit == EWorkoutSplit.TwoSplit) {
                setsPrMuscleGroup = 10;
                avgSecondsPrSet = 240;
            } else if (workoutSplit == EWorkoutSplit.ThreeSplit) {
                setsPrMuscleGroup = 15;
                avgSecondsPrSet = 240;
            }
        }

        for (MuscleGroup loadedMuscleGroup : muscleGroupsWithExercises) {
            loadedMuscleGroup.getExercises();
        }
    }

    private void VerifyDatabaseData(Context c) {
        MuscleGroupDao muscleGroupDao = DatabaseAccess.getInstance().getDatabase(c).muscleGroupDao();
        MuscleGroup[] allMuscleGroups = muscleGroupDao.selectAll();
        if (allMuscleGroups.length == 0) {
            addMuscleGroups(c);
        }

        ExerciseDao exerciseDao = DatabaseAccess.getInstance().getDatabase(c).exerciseDao();
        Exercise[] exercises = exerciseDao.selectAll();
        if (exercises.length == 0) {
            addExercises(c);
        }
    }

    private void addMuscleGroups(Context context) {
        DatabaseAccess.getInstance().getDatabase(context).muscleGroupDao().insertAll(muscleGroups());
    }

    private void addExercises(Context context) {
        DatabaseAccess.getInstance().getDatabase(context).exerciseDao().insertAll(exercises());
    }

    private static MuscleGroup[] muscleGroups() {
        List<MuscleGroup> muscleGroups = new ArrayList<>();

        MuscleGroup chest = new MuscleGroup();
        chest.setName(EMuscleGroup.Chest);
        List<EMuscleGroup> chestRecommendations = new ArrayList<>();
        chestRecommendations.add(EMuscleGroup.Triceps);
        chest.setRecommendedMuscleGroups(chestRecommendations);
        muscleGroups.add(chest);

        MuscleGroup shoulders = new MuscleGroup();
        shoulders.setName(EMuscleGroup.Shoulders);
        List<EMuscleGroup> shouldersRecommendations = new ArrayList<>();
        shouldersRecommendations.add(EMuscleGroup.Triceps);
        shoulders.setRecommendedMuscleGroups(shouldersRecommendations);
        muscleGroups.add(shoulders);

        MuscleGroup back = new MuscleGroup();
        back.setName(EMuscleGroup.Back);
        List<EMuscleGroup> backRecommendations = new ArrayList<>();
        backRecommendations.add(EMuscleGroup.Biceps);
        back.setRecommendedMuscleGroups(backRecommendations);
        muscleGroups.add(back);

        MuscleGroup legs = new MuscleGroup();
        legs.setName(EMuscleGroup.Legs);
        muscleGroups.add(legs);

        MuscleGroup triceps = new MuscleGroup();
        triceps.setName(EMuscleGroup.Triceps);
        List<EMuscleGroup> tricepsRecommendations = new ArrayList<>();
        tricepsRecommendations.add(EMuscleGroup.Chest);
        tricepsRecommendations.add(EMuscleGroup.Shoulders);
        triceps.setRecommendedMuscleGroups(tricepsRecommendations);
        muscleGroups.add(triceps);

        MuscleGroup biceps = new MuscleGroup();
        biceps.setName(EMuscleGroup.Biceps);
        List<EMuscleGroup> bicepsRecommendations = new ArrayList<>();
        bicepsRecommendations.add(EMuscleGroup.Back);
        biceps.setRecommendedMuscleGroups(bicepsRecommendations);
        muscleGroups.add(biceps);

        MuscleGroup[] muscleGroupArray = new MuscleGroup[muscleGroups.size()];
        for (int i = 0; i < muscleGroups.size(); i++) {
            muscleGroupArray[i] = muscleGroups.get(i);
        }

        return muscleGroupArray;
    }

    private static Exercise[] exercises() {
        List<Exercise> exercises = new ArrayList<>();

        //--- CHEST ---\\
        Exercise chest1 = new Exercise();
        chest1.setName("Bench Press");
        chest1.setMuscleGroup(EMuscleGroup.Chest);
        chest1.setDurationSeconds(60);
        chest1.setPauseDurationSeconds(150);
        chest1.setExerciseType(EExerciseType.Compound);
        exercises.add(chest1);

        Exercise chest2 = new Exercise();
        chest2.setName("Incline Bench Press");
        chest2.setMuscleGroup(EMuscleGroup.Chest);
        chest2.setDurationSeconds(60);
        chest2.setPauseDurationSeconds(150);
        chest2.setExerciseType(EExerciseType.Compound);
        exercises.add(chest2);

        Exercise chest3 = new Exercise();
        chest3.setName("Dumbbell Flyes");
        chest3.setMuscleGroup(EMuscleGroup.Chest);
        chest3.setDurationSeconds(45);
        chest3.setPauseDurationSeconds(90);
        chest3.setExerciseType(EExerciseType.Isolation);
        exercises.add(chest3);

        Exercise chest4 = new Exercise();
        chest4.setName("Cable Crossover");
        chest4.setMuscleGroup(EMuscleGroup.Chest);
        chest4.setDurationSeconds(45);
        chest4.setPauseDurationSeconds(90);
        chest4.setExerciseType(EExerciseType.Isolation);
        exercises.add(chest4);

        Exercise chest5 = new Exercise();
        chest5.setName("Incline Dumbbell Bench Press");
        chest5.setMuscleGroup(EMuscleGroup.Chest);
        chest5.setDurationSeconds(60);
        chest5.setPauseDurationSeconds(150);
        chest5.setExerciseType(EExerciseType.Compound);
        exercises.add(chest5);

        Exercise chest6 = new Exercise();
        chest6.setName("Decline Bench Press");
        chest6.setMuscleGroup(EMuscleGroup.Chest);
        chest6.setDurationSeconds(60);
        chest6.setPauseDurationSeconds(150);
        chest6.setExerciseType(EExerciseType.Compound);
        exercises.add(chest6);

        Exercise chest7 = new Exercise();
        chest7.setName("Incline Dumbbell Flyes");
        chest7.setMuscleGroup(EMuscleGroup.Chest);
        chest7.setDurationSeconds(45);
        chest7.setPauseDurationSeconds(150);
        chest7.setExerciseType(EExerciseType.Compound);
        exercises.add(chest7);

        Exercise chest8 = new Exercise();
        chest8.setName("Low-Cable Crossover");
        chest8.setMuscleGroup(EMuscleGroup.Chest);
        chest8.setDurationSeconds(45);
        chest8.setPauseDurationSeconds(90);
        chest8.setExerciseType(EExerciseType.Isolation);
        exercises.add(chest8);

        Exercise chest9 = new Exercise();
        chest9.setName("Hex Press");
        chest9.setMuscleGroup(EMuscleGroup.Chest);
        chest9.setDurationSeconds(45);
        chest9.setPauseDurationSeconds(90);
        chest9.setExerciseType(EExerciseType.Isolation);
        exercises.add(chest9);

        Exercise chest10 = new Exercise();
        chest10.setName("Flat Bench Dumbbell Press");
        chest10.setMuscleGroup(EMuscleGroup.Chest);
        chest10.setDurationSeconds(60);
        chest10.setPauseDurationSeconds(150);
        chest10.setExerciseType(EExerciseType.Compound);
        exercises.add(chest10);

        Exercise chest11 = new Exercise();
        chest11.setName("Machine Decline Press");
        chest11.setMuscleGroup(EMuscleGroup.Chest);
        chest11.setDurationSeconds(45);
        chest11.setPauseDurationSeconds(90);
        chest11.setExerciseType(EExerciseType.Isolation);
        exercises.add(chest11);

        Exercise chest12 = new Exercise();
        chest12.setName("Incline Bench Cable Flyes");
        chest12.setMuscleGroup(EMuscleGroup.Chest);
        chest12.setDurationSeconds(45);
        chest12.setPauseDurationSeconds(90);
        chest12.setExerciseType(EExerciseType.Isolation);
        exercises.add(chest12);

        Exercise chest13 = new Exercise();
        chest13.setName("Machine Flyes");
        chest13.setMuscleGroup(EMuscleGroup.Chest);
        chest13.setDurationSeconds(45);
        chest13.setPauseDurationSeconds(90);
        chest13.setExerciseType(EExerciseType.Isolation);
        exercises.add(chest13);

        //--- TRICEPS ---\\
        Exercise triceps1 = new Exercise();
        triceps1.setName("Skull Crushers");
        triceps1.setMuscleGroup(EMuscleGroup.Triceps);
        triceps1.setDurationSeconds(60);
        triceps1.setPauseDurationSeconds(90);
        triceps1.setExerciseType(EExerciseType.Isolation);
        exercises.add(triceps1);

        Exercise triceps2 = new Exercise();
        triceps2.setName("Rope Triceps Pressdown");
        triceps2.setMuscleGroup(EMuscleGroup.Triceps);
        triceps2.setDurationSeconds(45);
        triceps2.setPauseDurationSeconds(90);
        triceps2.setExerciseType(EExerciseType.Isolation);
        exercises.add(triceps2);

        Exercise triceps3 = new Exercise();
        triceps3.setName("Triceps Dips");
        triceps3.setMuscleGroup(EMuscleGroup.Triceps);
        triceps3.setDurationSeconds(45);
        triceps3.setPauseDurationSeconds(90);
        triceps3.setExerciseType(EExerciseType.Isolation);
        exercises.add(triceps3);

        Exercise triceps4 = new Exercise();
        triceps4.setName("Isolated Triceps Extensions");
        triceps4.setMuscleGroup(EMuscleGroup.Triceps);
        triceps4.setDurationSeconds(90);
        triceps4.setPauseDurationSeconds(90);
        triceps4.setExerciseType(EExerciseType.Isolation);
        exercises.add(triceps4);

        Exercise triceps5 = new Exercise();
        triceps1.setName("Underhand Triceps Pressdown");
        triceps1.setMuscleGroup(EMuscleGroup.Triceps);
        triceps1.setDurationSeconds(45);
        triceps1.setPauseDurationSeconds(90);
        triceps1.setExerciseType(EExerciseType.Isolation);
        exercises.add(triceps1);

        Exercise triceps6 = new Exercise();
        triceps1.setName("Kickbacks");
        triceps1.setMuscleGroup(EMuscleGroup.Triceps);
        triceps1.setDurationSeconds(45);
        triceps1.setPauseDurationSeconds(90);
        triceps1.setExerciseType(EExerciseType.Isolation);
        exercises.add(triceps1);

        Exercise triceps7 = new Exercise();
        triceps7.setName("Bar Triceps Pressdown");
        triceps7.setMuscleGroup(EMuscleGroup.Triceps);
        triceps7.setDurationSeconds(45);
        triceps7.setPauseDurationSeconds(90);
        triceps7.setExerciseType(EExerciseType.Isolation);
        exercises.add(triceps7);

        Exercise triceps8 = new Exercise();
        triceps8.setName("Close Grip Bench Press");
        triceps8.setMuscleGroup(EMuscleGroup.Triceps);
        triceps8.setDurationSeconds(45);
        triceps8.setPauseDurationSeconds(90);
        triceps8.setExerciseType(EExerciseType.Isolation);
        exercises.add(triceps8);

        Exercise triceps9 = new Exercise();
        triceps9.setName("Seated Overhead Dumbbell Extensions");
        triceps9.setMuscleGroup(EMuscleGroup.Triceps);
        triceps9.setDurationSeconds(45);
        triceps9.setPauseDurationSeconds(90);
        triceps9.setExerciseType(EExerciseType.Isolation);
        exercises.add(triceps9);

        Exercise triceps10 = new Exercise();
        triceps10.setName("Lying Dumbbell Triceps Extensions");
        triceps10.setMuscleGroup(EMuscleGroup.Triceps);
        triceps10.setDurationSeconds(45);
        triceps10.setPauseDurationSeconds(90);
        triceps10.setExerciseType(EExerciseType.Isolation);
        exercises.add(triceps10);

        Exercise triceps11 = new Exercise();
        triceps11.setName("Tate Press");
        triceps11.setMuscleGroup(EMuscleGroup.Triceps);
        triceps11.setDurationSeconds(60);
        triceps11.setPauseDurationSeconds(90);
        triceps11.setExerciseType(EExerciseType.Isolation);
        exercises.add(triceps11);

        //--- Shoulder ---\\
        Exercise shoulder1 = new Exercise();
        shoulder1.setName("Shoulder Press");
        shoulder1.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder1.setDurationSeconds(60);
        shoulder1.setPauseDurationSeconds(120);
        shoulder1.setExerciseType(EExerciseType.Compound);
        exercises.add(shoulder1);

        Exercise shoulder2 = new Exercise();
        shoulder2.setName("Barebell Overhead Press");
        shoulder2.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder2.setDurationSeconds(45);
        shoulder2.setPauseDurationSeconds(90);
        shoulder2.setExerciseType(EExerciseType.Compound);
        exercises.add(shoulder2);

        Exercise shoulder3 = new Exercise();
        shoulder3.setName("Standing Dumbbell Fly");
        shoulder3.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder3.setDurationSeconds(45);
        shoulder3.setPauseDurationSeconds(90);
        shoulder3.setExerciseType(EExerciseType.Compound);
        exercises.add(shoulder3);

        Exercise shoulder4 = new Exercise();
        shoulder4.setName("Face Pull");
        shoulder4.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder4.setDurationSeconds(45);
        shoulder4.setPauseDurationSeconds(90);
        shoulder4.setExerciseType(EExerciseType.Compound);
        exercises.add(shoulder4);

        Exercise shoulder5 = new Exercise();
        shoulder5.setName("High Pulls");
        shoulder5.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder5.setDurationSeconds(45);
        shoulder5.setPauseDurationSeconds(90);
        shoulder5.setExerciseType(EExerciseType.Compound);
        exercises.add(shoulder5);

        Exercise shoulder6 = new Exercise();
        shoulder6.setName("Incline Trap Raise");
        shoulder6.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder6.setDurationSeconds(45);
        shoulder6.setPauseDurationSeconds(90);
        shoulder6.setExerciseType(EExerciseType.Compound);
        exercises.add(shoulder6);

        Exercise shoulder7 = new Exercise();
        shoulder7.setName("Machine Shoulder Press");
        shoulder7.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder7.setDurationSeconds(45);
        shoulder7.setPauseDurationSeconds(120);
        shoulder7.setExerciseType(EExerciseType.Compound);
        exercises.add(shoulder7);

        Exercise shoulder8 = new Exercise();
        shoulder8.setName("Bend-Over Reverse Fly");
        shoulder8.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder8.setDurationSeconds(45);
        shoulder8.setPauseDurationSeconds(120);
        shoulder8.setExerciseType(EExerciseType.Compound);
        exercises.add(shoulder8);

        Exercise shoulder9 = new Exercise();
        shoulder9.setName("Arnold Press");
        shoulder9.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder9.setDurationSeconds(45);
        shoulder9.setPauseDurationSeconds(90);
        shoulder9.setExerciseType(EExerciseType.Compound);
        exercises.add(shoulder9);

        Exercise shoulder10 = new Exercise();
        shoulder10.setName("Lateral Raise");
        shoulder10.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder10.setDurationSeconds(45);
        shoulder10.setPauseDurationSeconds(90);
        shoulder10.setExerciseType(EExerciseType.Compound);
        exercises.add(shoulder10);

//TODO - Add more

        Exercise[] exerciseArray = new Exercise[exercises.size()];
        for (int i = 0; i < exercises.size(); i++) {
            exerciseArray[i] = exercises.get(i);
        }

        return exerciseArray;
    }
}

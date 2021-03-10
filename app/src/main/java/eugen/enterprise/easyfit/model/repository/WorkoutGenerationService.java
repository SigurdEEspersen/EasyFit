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

            List<MuscleGroup> workout = generateWorkout(workoutSplit, workoutDuration, muscleGroupsWithExercises, workoutExtras);

            callback.onResponse(workout);
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

    private List<MuscleGroup> generateWorkout(EWorkoutSplit workoutSplit, EWorkoutDuration workoutDuration, List<MuscleGroup> muscleGroupsWithExercises, EWorkoutExtras workoutExtras) {
        int setsPrMuscleGroup = 0;
        int avgSecondsPrSet = 0;
        int numberOfExercises = 0;

        if (workoutDuration == EWorkoutDuration.OneHour) {
            if (workoutSplit == EWorkoutSplit.FullBody) {
                setsPrMuscleGroup = 4;
                avgSecondsPrSet = 150;
                numberOfExercises = ThreadLocalRandom.current().nextInt(1, 2);
            } else if (workoutSplit == EWorkoutSplit.TwoSplit) {
                setsPrMuscleGroup = 6;
                avgSecondsPrSet = 180;
                numberOfExercises = ThreadLocalRandom.current().nextInt(2, 3);
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
                numberOfExercises = ThreadLocalRandom.current().nextInt(3, 4);
            }
        } else if (workoutDuration == EWorkoutDuration.TwoHours) {
            if (workoutSplit == EWorkoutSplit.FullBody) {
                setsPrMuscleGroup = 6;
                avgSecondsPrSet = 180;
                numberOfExercises = ThreadLocalRandom.current().nextInt(2, 3);
            } else if (workoutSplit == EWorkoutSplit.TwoSplit) {
                setsPrMuscleGroup = 12;
                avgSecondsPrSet = 200;
                numberOfExercises = ThreadLocalRandom.current().nextInt(3, 4);
            } else if (workoutSplit == EWorkoutSplit.ThreeSplit) {
                setsPrMuscleGroup = 15;
                avgSecondsPrSet = 240;
                int ThreeOrFive = ThreadLocalRandom.current().nextInt(0, 1);
                if (ThreeOrFive == 0) {
                    numberOfExercises = 3;
                } else {
                    numberOfExercises = 5;
                }
            }
        }

        int setsPrExercise = setsPrMuscleGroup / numberOfExercises;
        int totalSecondsPrMuscleGroup = avgSecondsPrSet * setsPrMuscleGroup;

        List<MuscleGroup> generatedWorkout = new ArrayList<>();

        Random random = new Random();
        for (MuscleGroup loadedMuscleGroup : muscleGroupsWithExercises) {
            List<Exercise> compoundExercises = new ArrayList<>();
            List<Exercise> isolationExercises = new ArrayList<>();

            for (Exercise exercise : loadedMuscleGroup.getExercises()) {
                if (exercise.getExerciseType().equals(EExerciseType.Compound)) {
                    compoundExercises.add(exercise);
                } else if (exercise.getExerciseType().equals(EExerciseType.Isolation)) {
                    isolationExercises.add(exercise);
                }
            }

            List<Exercise> finalExercises = new ArrayList<>();
            if (numberOfExercises == 1) {
                int index = random.nextInt(loadedMuscleGroup.getExercises().size());
                Exercise ex = loadedMuscleGroup.getExercises().get(index);
                finalExercises.add(ex); // Check for duration
            } else if (numberOfExercises == 2) {
                int indexCompound = random.nextInt(compoundExercises.size());
                compoundExercises.get(indexCompound);
                int indexIsolation = random.nextInt(compoundExercises.size());
                isolationExercises.get(indexIsolation);
            } else if (numberOfExercises == 3) {
                int indexCompound = random.nextInt(compoundExercises.size());
                compoundExercises.get(indexCompound);
                int indexIsolation = random.nextInt(compoundExercises.size());
                isolationExercises.get(indexIsolation);
                int index = random.nextInt(loadedMuscleGroup.getExercises().size());
                loadedMuscleGroup.getExercises().get(index); //Skal være forskellig fra compound/isolation exercise
            } else if (numberOfExercises == 4) {
                int indexCompound = random.nextInt(compoundExercises.size());
                compoundExercises.get(indexCompound);
                indexCompound = random.nextInt(compoundExercises.size());
                compoundExercises.get(indexCompound);
                int indexIsolation = random.nextInt(compoundExercises.size());
                isolationExercises.get(indexIsolation);
                indexIsolation = random.nextInt(compoundExercises.size());
                isolationExercises.get(indexIsolation);
            } else if (numberOfExercises == 5) {
                int indexCompound = random.nextInt(compoundExercises.size());
                compoundExercises.get(indexCompound);
                indexCompound = random.nextInt(compoundExercises.size());
                compoundExercises.get(indexCompound);
                int indexIsolation = random.nextInt(compoundExercises.size());
                isolationExercises.get(indexIsolation);
                indexIsolation = random.nextInt(compoundExercises.size());
                isolationExercises.get(indexIsolation);
                int index = random.nextInt(loadedMuscleGroup.getExercises().size());
                loadedMuscleGroup.getExercises().get(index); //Skal være forskellig fra compound/isolation exercise
            }

            loadedMuscleGroup.setExercises(finalExercises);
            generatedWorkout.add(loadedMuscleGroup);
        }

        return generatedWorkout;
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
        chest3.setName("Flyes");
        chest3.setMuscleGroup(EMuscleGroup.Chest);
        chest3.setDurationSeconds(60);
        chest3.setPauseDurationSeconds(90);
        chest3.setExerciseType(EExerciseType.Isolation);
        exercises.add(chest3);

        Exercise chest4 = new Exercise();
        chest4.setName("Cable Cross-over");
        chest4.setMuscleGroup(EMuscleGroup.Chest);
        chest4.setDurationSeconds(60);
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
        chest7.setName("Incline Dumbbell");
        chest7.setMuscleGroup(EMuscleGroup.Chest);
        chest7.setDurationSeconds(60);
        chest7.setPauseDurationSeconds(150);
        chest7.setExerciseType(EExerciseType.Compound);
        exercises.add(chest7);


        //--- TRICEPS ---\\
        Exercise triceps1 = new Exercise();
        triceps1.setName("Skull Crushers");
        triceps1.setMuscleGroup(EMuscleGroup.Triceps);
        triceps1.setDurationSeconds(60);
        triceps1.setPauseDurationSeconds(90);
        triceps1.setExerciseType(EExerciseType.Isolation);
        exercises.add(triceps1);

        //--- Shoulder ---\\
        Exercise shoulder1 = new Exercise();
        shoulder1.setName("Shoulder Press");
        shoulder1.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder1.setDurationSeconds(60);
        shoulder1.setPauseDurationSeconds(120);
        shoulder1.setExerciseType(EExerciseType.Compound);
        exercises.add(shoulder1);


//TODO - Add more

        Exercise[] exerciseArray = new Exercise[exercises.size()];
        for (int i = 0; i < exercises.size(); i++) {
            exerciseArray[i] = exercises.get(i);
        }

        return exerciseArray;
    }
}

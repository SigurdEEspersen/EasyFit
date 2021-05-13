package eugen.enterprise.easyfit.model.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import eugen.enterprise.easyfit.acquaintance.enums.EExerciseType;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutLoad;
import eugen.enterprise.easyfit.acquaintance.helpers.Workout;
import eugen.enterprise.easyfit.acquaintance.interfaces.Callback;
import eugen.enterprise.easyfit.acquaintance.enums.EMuscleGroup;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutDuration;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutExtras;
import eugen.enterprise.easyfit.acquaintance.enums.EWorkoutSplit;
import eugen.enterprise.easyfit.acquaintance.interfaces.IMuscleGroup;
import eugen.enterprise.easyfit.model.DatabaseAccess;
import eugen.enterprise.easyfit.model.data_objects.Exercise;
import eugen.enterprise.easyfit.model.data_objects.ExerciseDao;
import eugen.enterprise.easyfit.model.data_objects.MuscleGroup;
import eugen.enterprise.easyfit.model.data_objects.MuscleGroupDao;
import eugen.enterprise.easyfit.model.data_objects.PrefferedExercise;
import eugen.enterprise.easyfit.model.data_objects.PrefferedExerciseDao;

public class WorkoutGenerationService {

    public void createWorkout(EWorkoutSplit workoutSplit, EWorkoutDuration workoutDuration, List<EMuscleGroup> muscleGroups, EWorkoutExtras workoutExtras, boolean pre_workout, int extras_duration, Callback callback, Context c) {
        Runnable runnable = () -> {
            VerifyDatabaseData(c);

            List<MuscleGroup> muscleGroupsWithExercises = new ArrayList<>();

            for (EMuscleGroup eMuscleGroup : muscleGroups) {
                muscleGroupsWithExercises.add(getMuscleGroupWithExercises(c, eMuscleGroup));
            }

            PrefferedExerciseDao prefferedExerciseDao = DatabaseAccess.getInstance().getDatabase(c).prefferedExerciseDao();
            PrefferedExercise[] prefferedExercises = prefferedExerciseDao.selectAll();

            Workout workout = generateWorkout(workoutSplit, workoutDuration, muscleGroupsWithExercises, prefferedExercises, workoutExtras, pre_workout, extras_duration);

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

    private Workout generateWorkout(EWorkoutSplit workoutSplit, EWorkoutDuration workoutDuration, List<MuscleGroup> muscleGroupsWithExercises, PrefferedExercise[] prefferedExercises, EWorkoutExtras workoutExtras, boolean pre_workout, int extras_duration) {
        int setsPrMuscleGroup = 0;
        int avgSecondsPrSet = 0;
        int numberOfExercises = 0;

        Random random = new Random();

        if (workoutDuration == EWorkoutDuration.OneHour) {
            if (workoutSplit == EWorkoutSplit.FullBody) {
                setsPrMuscleGroup = 4;
                avgSecondsPrSet = 150;
                numberOfExercises = randomInt(random, 1, 2);
            } else if (workoutSplit == EWorkoutSplit.TwoSplit) {
                setsPrMuscleGroup = 6;
                avgSecondsPrSet = 180;
                numberOfExercises = randomInt(random, 2, 3);
            } else if (workoutSplit == EWorkoutSplit.ThreeSplit) {
                setsPrMuscleGroup = 9;
                avgSecondsPrSet = 180;
                numberOfExercises = 3;
            }
        } else if (workoutDuration == EWorkoutDuration.OneHalfHour) {
            if (workoutSplit == EWorkoutSplit.FullBody) {
                setsPrMuscleGroup = 6;
                avgSecondsPrSet = 150;
                numberOfExercises = randomInt(random, 2, 3);
            } else if (workoutSplit == EWorkoutSplit.TwoSplit) {
                setsPrMuscleGroup = 9;
                avgSecondsPrSet = 210;
                numberOfExercises = 3;
            } else if (workoutSplit == EWorkoutSplit.ThreeSplit) {
                setsPrMuscleGroup = 12;
                avgSecondsPrSet = 210;
                numberOfExercises = randomInt(random, 3, 4);
            }
        } else if (workoutDuration == EWorkoutDuration.TwoHours) {
            if (workoutSplit == EWorkoutSplit.FullBody) {
                setsPrMuscleGroup = 6;
                avgSecondsPrSet = 180;
                numberOfExercises = randomInt(random, 2, 3);
            } else if (workoutSplit == EWorkoutSplit.TwoSplit) {
                setsPrMuscleGroup = 12;
                avgSecondsPrSet = 200;
                numberOfExercises = randomInt(random, 3, 4);
            } else if (workoutSplit == EWorkoutSplit.ThreeSplit) {
                setsPrMuscleGroup = 15;
                avgSecondsPrSet = 240;
                int ThreeOrFive = randomInt(random, 0, 1);
                if (ThreeOrFive == 0) {
                    numberOfExercises = 3;
                } else {
                    numberOfExercises = 5;
                }
            }
        }

        int setsPrExercise = setsPrMuscleGroup / numberOfExercises;
        EWorkoutLoad workoutLoad;

        int workLoadSelection = randomInt(random, 0, 2);
        if (workLoadSelection == 0) {
            workoutLoad = EWorkoutLoad.Regular;
        } else if (workLoadSelection == 1) {
            workoutLoad = EWorkoutLoad.Medium;
        } else {
            workoutLoad = EWorkoutLoad.Heavy;
        }

        String prefferedChest = null;
        String prefferedShoulders = null;
        String prefferedBack = null;
        String prefferedLegs = null;
        String prefferedTriceps = null;
        String prefferedBiceps = null;

        for (PrefferedExercise preferredExercise : prefferedExercises) {
            switch (preferredExercise.getMuscleGroup()) {
                case "Chest":
                    prefferedChest = preferredExercise.getName();
                    break;
                case "Shoulders":
                    prefferedShoulders = preferredExercise.getName();
                    break;
                case "Back":
                    prefferedBack = preferredExercise.getName();
                    break;
                case "Legs":
                    prefferedLegs = preferredExercise.getName();
                    break;
                case "Triceps":
                    prefferedTriceps = preferredExercise.getName();
                    break;
                case "Biceps":
                    prefferedBiceps = preferredExercise.getName();
                    break;
            }
        }

        int numberOfExercisesToUse = numberOfExercises;
        List<IMuscleGroup> generatedWorkout = new ArrayList<>();
        int durationBuffer = 0; //Set to 0 for now. Can increase if needed
        for (MuscleGroup loadedMuscleGroup : muscleGroupsWithExercises) {
            int totalSecondsPrMuscleGroup = avgSecondsPrSet * setsPrMuscleGroup;
            List<Exercise> finalExercises = new ArrayList<>();

            List<Exercise> compoundExercises = new ArrayList<>();
            List<Exercise> isolationExercises = new ArrayList<>();

            for (Exercise exercise : loadedMuscleGroup.getExercises()) {
                if (exercise.getExerciseType().equals(EExerciseType.Compound.name())) {
                    compoundExercises.add(exercise);
                } else if (exercise.getExerciseType().equals(EExerciseType.Isolation.name())) {
                    isolationExercises.add(exercise);
                }
            }

            if (loadedMuscleGroup.getName().equals(EMuscleGroup.Chest.name())) {
                if (prefferedChest != null) {
                    for (Exercise exercise : loadedMuscleGroup.getExercises()) {
                        if (exercise.getName().equals(prefferedChest)) {
                            finalExercises.add(adjustPauseDuration(workoutLoad, exercise));
                            numberOfExercisesToUse--;
                            break;
                        }
                    }
                }
            } else if (loadedMuscleGroup.getName().equals(EMuscleGroup.Shoulders.name())) {
                if (prefferedShoulders != null) {
                    for (Exercise exercise : loadedMuscleGroup.getExercises()) {
                        if (exercise.getName().equals(prefferedShoulders)) {
                            finalExercises.add(adjustPauseDuration(workoutLoad, exercise));
                            numberOfExercisesToUse--;
                            break;
                        }
                    }
                }
            } else if (loadedMuscleGroup.getName().equals(EMuscleGroup.Back.name())) {
                if (prefferedBack != null) {
                    for (Exercise exercise : loadedMuscleGroup.getExercises()) {
                        if (exercise.getName().equals(prefferedBack)) {
                            finalExercises.add(adjustPauseDuration(workoutLoad, exercise));
                            numberOfExercisesToUse--;
                            break;
                        }
                    }
                }
            } else if (loadedMuscleGroup.getName().equals(EMuscleGroup.Legs.name())) {
                if (prefferedLegs != null) {
                    for (Exercise exercise : loadedMuscleGroup.getExercises()) {
                        if (exercise.getName().equals(prefferedLegs)) {
                            finalExercises.add(adjustPauseDuration(workoutLoad, exercise));
                            numberOfExercisesToUse--;
                            break;
                        }
                    }
                }
            } else if (loadedMuscleGroup.getName().equals(EMuscleGroup.Triceps.name())) {
                if (prefferedTriceps != null) {
                    for (Exercise exercise : loadedMuscleGroup.getExercises()) {
                        if (exercise.getName().equals(prefferedTriceps)) {
                            finalExercises.add(adjustPauseDuration(workoutLoad, exercise));
                            numberOfExercisesToUse--;
                            break;
                        }
                    }
                }
            } else if (loadedMuscleGroup.getName().equals(EMuscleGroup.Biceps.name())) {
                if (prefferedBiceps != null) {
                    for (Exercise exercise : loadedMuscleGroup.getExercises()) {
                        if (exercise.getName().equals(prefferedBiceps)) {
                            finalExercises.add(adjustPauseDuration(workoutLoad, exercise));
                            numberOfExercisesToUse--;
                            break;
                        }
                    }
                }
            }

            if (numberOfExercisesToUse == 1) {
                Collections.shuffle(loadedMuscleGroup.getExercises());

                for (Exercise randomExercise : loadedMuscleGroup.getExercises()) {
                    int exerciseTotalSeconds = randomExercise.getDurationSeconds() + randomExercise.getPauseDurationSeconds();
                    if (exerciseTotalSeconds <= avgSecondsPrSet + durationBuffer && !finalExercises.contains(randomExercise)) {
                        finalExercises.add(adjustPauseDuration(workoutLoad, randomExercise));
                        break;
                    }
                }
            } else if (numberOfExercisesToUse == 2) {
                Collections.shuffle(compoundExercises);
                Collections.shuffle(isolationExercises);

                for (Exercise randomExercise : compoundExercises) {
                    int exerciseTotalSeconds = randomExercise.getDurationSeconds() + randomExercise.getPauseDurationSeconds();
                    if (exerciseTotalSeconds <= avgSecondsPrSet + durationBuffer && !finalExercises.contains(randomExercise)) {
                        finalExercises.add(adjustPauseDuration(workoutLoad, randomExercise));
                        totalSecondsPrMuscleGroup = totalSecondsPrMuscleGroup - (exerciseTotalSeconds * setsPrExercise);
                        break;
                    }
                }

                for (Exercise randomExercise : isolationExercises) {
                    int exerciseTotalSeconds = randomExercise.getDurationSeconds() + randomExercise.getPauseDurationSeconds();
                    int avgSecondsWithBuffer = (totalSecondsPrMuscleGroup / setsPrExercise) + durationBuffer;
                    if (exerciseTotalSeconds <= avgSecondsWithBuffer && !finalExercises.contains(randomExercise)) {
                        finalExercises.add(adjustPauseDuration(workoutLoad, randomExercise));
                        break;
                    }
                }
            } else if (numberOfExercisesToUse == 3) {
                Collections.shuffle(compoundExercises);
                Collections.shuffle(isolationExercises);
                Collections.shuffle(loadedMuscleGroup.getExercises());

                for (Exercise randomExercise : compoundExercises) {
                    int exerciseTotalSeconds = randomExercise.getDurationSeconds() + randomExercise.getPauseDurationSeconds();
                    if (exerciseTotalSeconds <= avgSecondsPrSet + durationBuffer && !finalExercises.contains(randomExercise)) {
                        finalExercises.add(adjustPauseDuration(workoutLoad, randomExercise));
                        totalSecondsPrMuscleGroup = totalSecondsPrMuscleGroup - (exerciseTotalSeconds * setsPrExercise);
                        break;
                    }
                }

                for (Exercise randomExercise : isolationExercises) {
                    int exerciseTotalSeconds = randomExercise.getDurationSeconds() + randomExercise.getPauseDurationSeconds();
                    int avgSecondsWithBuffer = ((totalSecondsPrMuscleGroup / 2) / setsPrExercise) + durationBuffer;
                    if (exerciseTotalSeconds <= avgSecondsWithBuffer && !finalExercises.contains(randomExercise)) {
                        finalExercises.add(adjustPauseDuration(workoutLoad, randomExercise));
                        totalSecondsPrMuscleGroup = totalSecondsPrMuscleGroup - (exerciseTotalSeconds * setsPrExercise);
                        break;
                    }
                }

                for (Exercise randomExercise : loadedMuscleGroup.getExercises()) {
                    int exerciseTotalSeconds = randomExercise.getDurationSeconds() + randomExercise.getPauseDurationSeconds();
                    int avgSecondsWithBuffer = (totalSecondsPrMuscleGroup / setsPrExercise) + durationBuffer;
                    if (exerciseTotalSeconds <= avgSecondsWithBuffer && !finalExercises.contains(randomExercise)) {
                        finalExercises.add(adjustPauseDuration(workoutLoad, randomExercise));
                        break;
                    }
                }
            } else if (numberOfExercisesToUse == 4) {
                Collections.shuffle(compoundExercises);
                Collections.shuffle(isolationExercises);

                for (Exercise randomExercise : compoundExercises) {
                    int exerciseTotalSeconds = randomExercise.getDurationSeconds() + randomExercise.getPauseDurationSeconds();
                    if (exerciseTotalSeconds <= avgSecondsPrSet + durationBuffer && !finalExercises.contains(randomExercise)) {
                        finalExercises.add(adjustPauseDuration(workoutLoad, randomExercise));
                        totalSecondsPrMuscleGroup = totalSecondsPrMuscleGroup - (exerciseTotalSeconds * setsPrExercise);
                        break;
                    }
                }

                for (Exercise randomExercise : isolationExercises) {
                    int exerciseTotalSeconds = randomExercise.getDurationSeconds() + randomExercise.getPauseDurationSeconds();
                    int avgSecondsWithBuffer = ((totalSecondsPrMuscleGroup / 3) / setsPrExercise) + durationBuffer;
                    if (exerciseTotalSeconds <= avgSecondsWithBuffer && !finalExercises.contains(randomExercise)) {
                        finalExercises.add(adjustPauseDuration(workoutLoad, randomExercise));
                        totalSecondsPrMuscleGroup = totalSecondsPrMuscleGroup - (exerciseTotalSeconds * setsPrExercise);
                        break;
                    }
                }

                for (Exercise randomExercise : compoundExercises) {
                    int exerciseTotalSeconds = randomExercise.getDurationSeconds() + randomExercise.getPauseDurationSeconds();
                    int avgSecondsWithBuffer = ((totalSecondsPrMuscleGroup / 2) / setsPrExercise) + durationBuffer;
                    if (exerciseTotalSeconds <= avgSecondsWithBuffer && !finalExercises.contains(randomExercise)) {
                        finalExercises.add(adjustPauseDuration(workoutLoad, randomExercise));
                        totalSecondsPrMuscleGroup = totalSecondsPrMuscleGroup - (exerciseTotalSeconds * setsPrExercise);
                        break;
                    }
                }

                for (Exercise randomExercise : isolationExercises) {
                    int exerciseTotalSeconds = randomExercise.getDurationSeconds() + randomExercise.getPauseDurationSeconds();
                    int avgSecondsWithBuffer = (totalSecondsPrMuscleGroup / setsPrExercise) + durationBuffer;
                    if (exerciseTotalSeconds <= avgSecondsWithBuffer && !finalExercises.contains(randomExercise)) {
                        finalExercises.add(adjustPauseDuration(workoutLoad, randomExercise));
                        break;
                    }
                }
            } else if (numberOfExercisesToUse == 5) {
                Collections.shuffle(compoundExercises);
                Collections.shuffle(isolationExercises);
                Collections.shuffle(loadedMuscleGroup.getExercises());

                for (Exercise randomExercise : compoundExercises) {
                    int exerciseTotalSeconds = randomExercise.getDurationSeconds() + randomExercise.getPauseDurationSeconds();
                    if (exerciseTotalSeconds <= avgSecondsPrSet + durationBuffer && !finalExercises.contains(randomExercise)) {
                        finalExercises.add(adjustPauseDuration(workoutLoad, randomExercise));
                        totalSecondsPrMuscleGroup = totalSecondsPrMuscleGroup - (exerciseTotalSeconds * setsPrExercise);
                        break;
                    }
                }

                for (Exercise randomExercise : isolationExercises) {
                    int exerciseTotalSeconds = randomExercise.getDurationSeconds() + randomExercise.getPauseDurationSeconds();
                    int avgSecondsWithBuffer = ((totalSecondsPrMuscleGroup / 4) / setsPrExercise) + durationBuffer;
                    if (exerciseTotalSeconds <= avgSecondsWithBuffer && !finalExercises.contains(randomExercise)) {
                        finalExercises.add(adjustPauseDuration(workoutLoad, randomExercise));
                        totalSecondsPrMuscleGroup = totalSecondsPrMuscleGroup - (exerciseTotalSeconds * setsPrExercise);
                        break;
                    }
                }

                for (Exercise randomExercise : compoundExercises) {
                    int exerciseTotalSeconds = randomExercise.getDurationSeconds() + randomExercise.getPauseDurationSeconds();
                    int avgSecondsWithBuffer = ((totalSecondsPrMuscleGroup / 3) / setsPrExercise) + durationBuffer;
                    if (exerciseTotalSeconds <= avgSecondsWithBuffer && !finalExercises.contains(randomExercise)) {
                        finalExercises.add(adjustPauseDuration(workoutLoad, randomExercise));
                        totalSecondsPrMuscleGroup = totalSecondsPrMuscleGroup - (exerciseTotalSeconds * setsPrExercise);
                        break;
                    }
                }

                for (Exercise randomExercise : isolationExercises) {
                    int exerciseTotalSeconds = randomExercise.getDurationSeconds() + randomExercise.getPauseDurationSeconds();
                    int avgSecondsWithBuffer = ((totalSecondsPrMuscleGroup / 2) / setsPrExercise) + durationBuffer;
                    if (exerciseTotalSeconds <= avgSecondsWithBuffer && !finalExercises.contains(randomExercise)) {
                        finalExercises.add(adjustPauseDuration(workoutLoad, randomExercise));
                        totalSecondsPrMuscleGroup = totalSecondsPrMuscleGroup - (exerciseTotalSeconds * setsPrExercise);
                        break;
                    }
                }

                for (Exercise randomExercise : loadedMuscleGroup.getExercises()) {
                    int exerciseTotalSeconds = randomExercise.getDurationSeconds() + randomExercise.getPauseDurationSeconds();
                    int avgSecondsWithBuffer = (totalSecondsPrMuscleGroup / setsPrExercise) + durationBuffer;
                    if (exerciseTotalSeconds <= avgSecondsWithBuffer && !finalExercises.contains(randomExercise)) {
                        finalExercises.add(adjustPauseDuration(workoutLoad, randomExercise));
                        break;
                    }
                }
            }

            loadedMuscleGroup.setExercises(finalExercises);
            generatedWorkout.add(loadedMuscleGroup);
            numberOfExercisesToUse = numberOfExercises;
        }

        if (workoutExtras != null) {
            MuscleGroup extras = new MuscleGroup();
            extras.workoutExtra(workoutExtras, extras_duration);
            if (pre_workout) {
                generatedWorkout.add(0, extras);
            } else {
                generatedWorkout.add(extras);
            }
        }

        Workout workout = new Workout();
        workout.setMuscleGroups(generatedWorkout);
        workout.setWorkoutLoad(workoutLoad);
        workout.setSetsPrExercise(setsPrExercise);
        return workout;
    }

    private Exercise adjustPauseDuration(EWorkoutLoad workLoad, Exercise exercise) {
        int adjustedPause;
        switch (workLoad) {
            case Regular:
                break;
            case Medium:
                adjustedPause = exercise.getPauseDurationSeconds() + (Math.round(exercise.getDurationSeconds() / 4));
                exercise.setPauseDurationSeconds(adjustedPause);
                break;
            case Heavy:
                adjustedPause = exercise.getPauseDurationSeconds() + (Math.round(exercise.getDurationSeconds() / 2));
                exercise.setPauseDurationSeconds(adjustedPause);
                break;
        }
        return exercise;
    }

    public static int randomInt(Random random, int min, int max) {
        int randomNum = random.nextInt((max - min) + 1) + min;
        return randomNum;
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

        addChestExercises(exercises);
        addShoulderExercises(exercises);
        addBackExercises(exercises);
        addLegExercises(exercises);
        addTricepsExercises(exercises);
        addBicepsExercises(exercises);

        Exercise[] exerciseArray = new Exercise[exercises.size()];
        for (int i = 0; i < exercises.size(); i++) {
            exerciseArray[i] = exercises.get(i);
        }

        return exerciseArray;
    }

    private static void addChestExercises(List<Exercise> exercises) {
        Exercise chest1 = new Exercise();
        chest1.setName("Bench Press");
        chest1.setMuscleGroup(EMuscleGroup.Chest);
        chest1.setDurationSeconds(60);
        chest1.setPauseDurationSeconds(150);
        chest1.setExerciseType(EExerciseType.Compound);
        chest1.setExerciseLink("https://www.coachmag.co.uk/exercises/chest-exercises/186/bench-press-technique-tips");
        exercises.add(chest1);

        Exercise chest2 = new Exercise();
        chest2.setName("Incline Bench Press");
        chest2.setMuscleGroup(EMuscleGroup.Chest);
        chest2.setDurationSeconds(60);
        chest2.setPauseDurationSeconds(150);
        chest2.setExerciseType(EExerciseType.Compound);
        chest2.setExerciseLink("https://www.coachmag.co.uk/chest-exercises/8686/incline-bench-press");
        exercises.add(chest2);

        Exercise chest3 = new Exercise();
        chest3.setName("Dumbbell Flyes");
        chest3.setMuscleGroup(EMuscleGroup.Chest);
        chest3.setDurationSeconds(45);
        chest3.setPauseDurationSeconds(90);
        chest3.setExerciseType(EExerciseType.Isolation);
        chest3.setExerciseLink("https://www.coachmag.co.uk/dumbbell-exercises/6709/how-to-do-the-chest-building-dumbbell-flye");
        exercises.add(chest3);

        Exercise chest4 = new Exercise();
        chest4.setName("Cable Crossover");
        chest4.setMuscleGroup(EMuscleGroup.Chest);
        chest4.setDurationSeconds(45);
        chest4.setPauseDurationSeconds(90);
        chest4.setExerciseType(EExerciseType.Isolation);
        chest4.setExerciseLink("https://www.coachmag.co.uk/chest-exercises/6623/the-cable-flye-is-our-go-to-chest-building-move");
        exercises.add(chest4);

        Exercise chest5 = new Exercise();
        chest5.setName("Incline Dumbbell Bench Press");
        chest5.setMuscleGroup(EMuscleGroup.Chest);
        chest5.setDurationSeconds(60);
        chest5.setPauseDurationSeconds(150);
        chest5.setExerciseType(EExerciseType.Compound);
        chest5.setExerciseLink("https://www.verywellfit.com/how-to-do-incline-dumbbell-press-4588212");
        exercises.add(chest5);

        Exercise chest6 = new Exercise();
        chest6.setName("Decline Bench Press");
        chest6.setMuscleGroup(EMuscleGroup.Chest);
        chest6.setDurationSeconds(60);
        chest6.setPauseDurationSeconds(150);
        chest6.setExerciseType(EExerciseType.Compound);
        chest6.setExerciseLink("https://www.myprotein.com/thezone/training/decline-bench-press/");
        exercises.add(chest6);

        Exercise chest7 = new Exercise();
        chest7.setName("Incline Dumbbell Flyes");
        chest7.setMuscleGroup(EMuscleGroup.Chest);
        chest7.setDurationSeconds(45);
        chest7.setPauseDurationSeconds(150);
        chest7.setExerciseType(EExerciseType.Isolation);
        chest7.setExerciseLink("https://www.healthline.com/health/exercise-fitness/dumbbell-chest-fly#safety");
        exercises.add(chest7);

        Exercise chest8 = new Exercise();
        chest8.setName("Low-Cable Crossover");
        chest8.setMuscleGroup(EMuscleGroup.Chest);
        chest8.setDurationSeconds(45);
        chest8.setPauseDurationSeconds(90);
        chest8.setExerciseType(EExerciseType.Isolation);
        chest8.setExerciseLink("https://fitnessvolt.com/23961/low-cable-cross-over/");
        exercises.add(chest8);

        Exercise chest9 = new Exercise();
        chest9.setName("Hex Press");
        chest9.setMuscleGroup(EMuscleGroup.Chest);
        chest9.setDurationSeconds(45);
        chest9.setPauseDurationSeconds(90);
        chest9.setExerciseType(EExerciseType.Isolation);
        chest9.setExerciseLink("https://www.youtube.com/watch?v=yTtoMu4tz_I");
        exercises.add(chest9);

        Exercise chest10 = new Exercise();
        chest10.setName("Dumbbell Bench Press");
        chest10.setMuscleGroup(EMuscleGroup.Chest);
        chest10.setDurationSeconds(60);
        chest10.setPauseDurationSeconds(150);
        chest10.setExerciseType(EExerciseType.Compound);
        chest10.setExerciseLink("https://www.coachmag.co.uk/chest-exercises/7391/how-to-do-the-dumbbell-bench-press");
        exercises.add(chest10);

        Exercise chest11 = new Exercise();
        chest11.setName("Machine Decline Press");
        chest11.setMuscleGroup(EMuscleGroup.Chest);
        chest11.setDurationSeconds(45);
        chest11.setPauseDurationSeconds(90);
        chest11.setExerciseType(EExerciseType.Isolation);
        chest11.setExerciseLink("https://www.jefit.com/exercises/179/machine-decline-chest-press");
        exercises.add(chest11);

        Exercise chest12 = new Exercise();
        chest12.setName("Incline Bench Cable Flyes");
        chest12.setMuscleGroup(EMuscleGroup.Chest);
        chest12.setDurationSeconds(45);
        chest12.setPauseDurationSeconds(90);
        chest12.setExerciseType(EExerciseType.Isolation);
        chest12.setExerciseLink("https://www.americangrit.com/2017/09/04/incline-cable-flyes-wider-chest/");
        exercises.add(chest12);

        Exercise chest13 = new Exercise();
        chest13.setName("Machine Flyes");
        chest13.setMuscleGroup(EMuscleGroup.Chest);
        chest13.setDurationSeconds(45);
        chest13.setPauseDurationSeconds(90);
        chest13.setExerciseType(EExerciseType.Isolation);
        chest13.setExerciseLink("https://www.verywellfit.com/how-to-use-a-chest-fly-machine-4589757");
        exercises.add(chest13);

        Exercise chest14 = new Exercise();
        chest14.setName("Decline Dumbbell Bench Press");
        chest14.setMuscleGroup(EMuscleGroup.Chest);
        chest14.setDurationSeconds(60);
        chest14.setPauseDurationSeconds(150);
        chest14.setExerciseType(EExerciseType.Compound);
        chest14.setExerciseLink("https://weighttraining.guide/exercises/decline-dumbbell-bench-press/");
        exercises.add(chest14);
    }

    private static void addShoulderExercises(List<Exercise> exercises) {
        Exercise shoulder1 = new Exercise();
        shoulder1.setName("Shoulder Press");
        shoulder1.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder1.setDurationSeconds(60);
        shoulder1.setPauseDurationSeconds(120);
        shoulder1.setExerciseType(EExerciseType.Compound);
        shoulder1.setExerciseLink("https://www.coachmag.co.uk/shoulder-exercises/6134/how-to-do-a-dumbbell-shoulder-press");
        exercises.add(shoulder1);

        Exercise shoulder2 = new Exercise();
        shoulder2.setName("Barbell Overhead Press");
        shoulder2.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder2.setDurationSeconds(45);
        shoulder2.setPauseDurationSeconds(90);
        shoulder2.setExerciseType(EExerciseType.Compound);
        shoulder2.setExerciseLink("https://www.coachmag.co.uk/exercises/shoulder-exercises/203/perfect-the-shoulder-press");
        exercises.add(shoulder2);

        Exercise shoulder3 = new Exercise();
        shoulder3.setName("Face Pulls");
        shoulder3.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder3.setDurationSeconds(60);
        shoulder3.setPauseDurationSeconds(90);
        shoulder3.setExerciseType(EExerciseType.Compound);
        shoulder3.setExerciseLink("https://www.coachmag.co.uk/exercises/shoulder-exercises/3274/move-week-face-pull");
        exercises.add(shoulder3);

        Exercise shoulder4 = new Exercise();
        shoulder4.setName("High Pulls");
        shoulder4.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder4.setDurationSeconds(60);
        shoulder4.setPauseDurationSeconds(90);
        shoulder4.setExerciseType(EExerciseType.Isolation);
        shoulder4.setExerciseLink("https://www.coachmag.co.uk/shoulder-exercises/6736/how-to-master-the-high-pull");
        exercises.add(shoulder4);

        Exercise shoulder5 = new Exercise();
        shoulder5.setName("Incline Trap Raise");
        shoulder5.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder5.setDurationSeconds(45);
        shoulder5.setPauseDurationSeconds(90);
        shoulder5.setExerciseType(EExerciseType.Isolation);
        shoulder5.setExerciseLink("https://www.msn.com/en-us/health/exercise/strength/incline-y-raise/ss-BBtOa6U");
        exercises.add(shoulder5);

        Exercise shoulder6 = new Exercise();
        shoulder6.setName("Machine Shoulder Press");
        shoulder6.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder6.setDurationSeconds(60);
        shoulder6.setPauseDurationSeconds(120);
        shoulder6.setExerciseType(EExerciseType.Compound);
        shoulder6.setExerciseLink("https://www.muscleandstrength.com/exercises/machine-shoulder-press");
        exercises.add(shoulder6);

        Exercise shoulder7 = new Exercise();
        shoulder7.setName("Bend-Over Reverse Fly");
        shoulder7.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder7.setDurationSeconds(45);
        shoulder7.setPauseDurationSeconds(120);
        shoulder7.setExerciseType(EExerciseType.Isolation);
        shoulder7.setExerciseLink("https://www.muscleandstrength.com/exercises/bent-over-dumbbell-reverse-fly.html");
        exercises.add(shoulder7);

        Exercise shoulder8 = new Exercise();
        shoulder8.setName("Arnold Press");
        shoulder8.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder8.setDurationSeconds(60);
        shoulder8.setPauseDurationSeconds(90);
        shoulder8.setExerciseType(EExerciseType.Compound);
        shoulder8.setExerciseLink("https://www.coachmag.co.uk/exercises/arm-exercises/2987/6-standing-dumb-bell-curl-arnold-press");
        exercises.add(shoulder8);

        Exercise shoulder9 = new Exercise();
        shoulder9.setName("Lateral Raise");
        shoulder9.setMuscleGroup(EMuscleGroup.Shoulders);
        shoulder9.setDurationSeconds(45);
        shoulder9.setPauseDurationSeconds(90);
        shoulder9.setExerciseType(EExerciseType.Isolation);
        shoulder9.setExerciseLink("https://www.coachmag.co.uk/exercises/shoulder-exercises/206/lateral-raises-%E2%80%93-how-do-them-and-why-you-should");
        exercises.add(shoulder9);
    }

    private static void addBackExercises(List<Exercise> exercises) {
        Exercise back1 = new Exercise();
        back1.setName("Deadlift");
        back1.setMuscleGroup(EMuscleGroup.Back);
        back1.setDurationSeconds(120);
        back1.setPauseDurationSeconds(90);
        back1.setExerciseType(EExerciseType.Compound);
        back1.setExerciseLink("https://www.coachmag.co.uk/barbell-exercises/3725/how-to-deadlift");
        exercises.add(back1);

        Exercise back2 = new Exercise();
        back2.setName("Wide Grip Pull Ups");
        back2.setMuscleGroup(EMuscleGroup.Back);
        back2.setDurationSeconds(60);
        back2.setPauseDurationSeconds(90);
        back2.setExerciseType(EExerciseType.Compound);
        back2.setExerciseLink("https://www.coachmag.co.uk/exercises/full-body-workouts/2534/10-pull-variations");
        exercises.add(back2);

        Exercise back3 = new Exercise();
        back3.setName("Machine Row");
        back3.setMuscleGroup(EMuscleGroup.Back);
        back3.setDurationSeconds(60);
        back3.setPauseDurationSeconds(90);
        back3.setExerciseType(EExerciseType.Compound);
        back3.setExerciseLink("https://www.muscleandstrength.com/exercises/machine-row.html");
        exercises.add(back3);

        Exercise back4 = new Exercise();
        back4.setName("Close Grip Pull ups");
        back4.setMuscleGroup(EMuscleGroup.Back);
        back4.setDurationSeconds(60);
        back4.setPauseDurationSeconds(90);
        back4.setExerciseType(EExerciseType.Compound);
        back4.setExerciseLink("https://www.muscleandstrength.com/exercises/close-grip-pull-up.html");
        exercises.add(back4);

        Exercise back5 = new Exercise();
        back5.setName("One-Arm Dumbbell Rows");
        back5.setMuscleGroup(EMuscleGroup.Back);
        back5.setDurationSeconds(45);
        back5.setPauseDurationSeconds(90);
        back5.setExerciseType(EExerciseType.Compound);
        back5.setExerciseLink("https://www.coachmag.co.uk/dumbbell-exercises/7380/how-to-do-the-one-arm-dumbbell-row");
        exercises.add(back5);

        Exercise back6 = new Exercise();
        back6.setName("Hyper Extensions");
        back6.setMuscleGroup(EMuscleGroup.Back);
        back6.setDurationSeconds(60);
        back6.setPauseDurationSeconds(90);
        back6.setExerciseType(EExerciseType.Compound);
        back6.setExerciseLink("https://www.healthline.com/health/back-extension-exercise#on-a-bench");
        exercises.add(back6);

        Exercise back7 = new Exercise();
        back7.setName("Seated Cable Row");
        back7.setMuscleGroup(EMuscleGroup.Back);
        back7.setDurationSeconds(60);
        back7.setPauseDurationSeconds(90);
        back7.setExerciseType(EExerciseType.Compound);
        back7.setExerciseLink("https://www.coachmag.co.uk/exercises/6917/how-to-do-the-cable-row");
        exercises.add(back7);

        Exercise back8 = new Exercise();
        back8.setName("T-Bar Row");
        back8.setMuscleGroup(EMuscleGroup.Back);
        back8.setDurationSeconds(120);
        back8.setPauseDurationSeconds(90);
        back8.setExerciseType(EExerciseType.Compound);
        back8.setExerciseLink("https://www.muscleandfitness.com/workouts/back-exercises/how-t-bar-row/");
        exercises.add(back8);

        Exercise back9 = new Exercise();
        back9.setName("Pullovers");
        back9.setMuscleGroup(EMuscleGroup.Back);
        back9.setDurationSeconds(45);
        back9.setPauseDurationSeconds(90);
        back9.setExerciseType(EExerciseType.Isolation);
        back9.setExerciseLink("https://www.verywellfit.com/how-to-dumbbell-pullover-techniques-benefits-variations-4781595");
        exercises.add(back9);

        Exercise back10 = new Exercise();
        back10.setName("Lat Pulldown");
        back10.setMuscleGroup(EMuscleGroup.Back);
        back10.setDurationSeconds(60);
        back10.setPauseDurationSeconds(90);
        back10.setExerciseType(EExerciseType.Isolation);
        back10.setExerciseLink("https://www.coachmag.co.uk/chest-exercises/182/how-to-do-the-lat-pull-down");
        exercises.add(back10);
    }

    private static void addLegExercises(List<Exercise> exercises) {
        Exercise legs1 = new Exercise();
        legs1.setName("Squat");
        legs1.setMuscleGroup(EMuscleGroup.Legs);
        legs1.setDurationSeconds(60);
        legs1.setPauseDurationSeconds(150);
        legs1.setExerciseType(EExerciseType.Compound);
        legs1.setExerciseLink("https://www.coachmag.co.uk/exercises/leg-exercises/193/squat-tips-for-perfect-form");
        exercises.add(legs1);

        Exercise legs2 = new Exercise();
        legs2.setName("Lunges");
        legs2.setMuscleGroup(EMuscleGroup.Legs);
        legs2.setDurationSeconds(60);
        legs2.setPauseDurationSeconds(90);
        legs2.setExerciseType(EExerciseType.Compound);
        legs2.setExerciseLink("https://www.coachmag.co.uk/exercises/leg-exercises/197/lunge");
        exercises.add(legs2);

        Exercise legs3 = new Exercise();
        legs3.setName("Leg Press");
        legs3.setMuscleGroup(EMuscleGroup.Legs);
        legs3.setDurationSeconds(60);
        legs3.setPauseDurationSeconds(120);
        legs3.setExerciseType(EExerciseType.Compound);
        legs3.setExerciseLink("https://www.coachmag.co.uk/exercises/leg-exercises/3500/leg-press-versus-squats-which-should-you-do");
        exercises.add(legs3);

        Exercise legs4 = new Exercise();
        legs4.setName("Leg Extension");
        legs4.setMuscleGroup(EMuscleGroup.Legs);
        legs4.setDurationSeconds(45);
        legs4.setPauseDurationSeconds(90);
        legs4.setExerciseType(EExerciseType.Isolation);
        legs4.setExerciseLink("https://www.coachmag.co.uk/leg-exercises/7523/how-to-do-the-leg-extension");
        exercises.add(legs4);

        Exercise legs5 = new Exercise();
        legs5.setName("Leg Curl");
        legs5.setMuscleGroup(EMuscleGroup.Legs);
        legs5.setDurationSeconds(45);
        legs5.setPauseDurationSeconds(90);
        legs5.setExerciseType(EExerciseType.Isolation);
        legs5.setExerciseLink("https://www.coachmag.co.uk/leg-exercises/7455/how-to-do-the-leg-curl");
        exercises.add(legs5);

        Exercise legs6 = new Exercise();
        legs6.setName("Seated Calf Raise");
        legs6.setMuscleGroup(EMuscleGroup.Legs);
        legs6.setDurationSeconds(45);
        legs6.setPauseDurationSeconds(60);
        legs6.setExerciseType(EExerciseType.Isolation);
        legs6.setExerciseLink("https://www.muscleandstrength.com/exercises/seated-calf-raise.html");
        exercises.add(legs6);

        Exercise legs7 = new Exercise();
        legs7.setName("Barbell Hip Thrust");
        legs7.setMuscleGroup(EMuscleGroup.Legs);
        legs7.setDurationSeconds(45);
        legs7.setPauseDurationSeconds(90);
        legs7.setExerciseType(EExerciseType.Isolation);
        legs7.setExerciseLink("https://www.coachmag.co.uk/glute-exercises/8004/how-to-do-the-hip-thrust");
        exercises.add(legs7);

        Exercise legs8 = new Exercise();
        legs8.setName("Standing Dumbbell Calf Raise");
        legs8.setMuscleGroup(EMuscleGroup.Legs);
        legs8.setDurationSeconds(45);
        legs8.setPauseDurationSeconds(60);
        legs8.setExerciseType(EExerciseType.Isolation);
        legs8.setExerciseLink("https://www.menshealth.com/uk/fitness/a735436/dumbbell-calf-raise/");
        exercises.add(legs8);

        Exercise legs9 = new Exercise();
        legs9.setName("Front Squat");
        legs9.setMuscleGroup(EMuscleGroup.Legs);
        legs9.setDurationSeconds(60);
        legs9.setPauseDurationSeconds(150);
        legs9.setExerciseType(EExerciseType.Compound);
        legs9.setExerciseLink("https://www.coachmag.co.uk/leg-exercises/3501/front-squat-the-best-exercise-you-aren-t-doing");
        exercises.add(legs9);

        Exercise legs10 = new Exercise();
        legs10.setName("Hack Squat");
        legs10.setMuscleGroup(EMuscleGroup.Legs);
        legs10.setDurationSeconds(60);
        legs10.setPauseDurationSeconds(120);
        legs10.setExerciseType(EExerciseType.Compound);
        legs10.setExerciseLink("https://www.muscleandstrength.com/exercises/hack-squat.html");
        exercises.add(legs10);
    }

    private static void addTricepsExercises(List<Exercise> exercises) {
        Exercise triceps1 = new Exercise();
        triceps1.setName("Skull Crushers");
        triceps1.setMuscleGroup(EMuscleGroup.Triceps);
        triceps1.setDurationSeconds(60);
        triceps1.setPauseDurationSeconds(90);
        triceps1.setExerciseType(EExerciseType.Compound);
        triceps1.setExerciseLink("https://www.menshealth.com/fitness/a25410119/skull-crushers/");
        exercises.add(triceps1);

        Exercise triceps2 = new Exercise();
        triceps2.setName("Rope Triceps Pressdown");
        triceps2.setMuscleGroup(EMuscleGroup.Triceps);
        triceps2.setDurationSeconds(45);
        triceps2.setPauseDurationSeconds(90);
        triceps2.setExerciseType(EExerciseType.Isolation);
        triceps2.setExerciseLink("https://www.coachmag.co.uk/exercises/arm-exercises/162/triceps-press-down");
        exercises.add(triceps2);

        Exercise triceps3 = new Exercise();
        triceps3.setName("Triceps Dips");
        triceps3.setMuscleGroup(EMuscleGroup.Triceps);
        triceps3.setDurationSeconds(60);
        triceps3.setPauseDurationSeconds(90);
        triceps3.setExerciseType(EExerciseType.Compound);
        triceps3.setExerciseLink("https://www.coachmag.co.uk/exercises/arm-exercises/156/bench-dip");
        exercises.add(triceps3);

        Exercise triceps4 = new Exercise();
        triceps4.setName("Isolated Triceps Extensions");
        triceps4.setMuscleGroup(EMuscleGroup.Triceps);
        triceps4.setDurationSeconds(90);
        triceps4.setPauseDurationSeconds(90);
        triceps4.setExerciseType(EExerciseType.Isolation);
        triceps4.setExerciseLink("https://www.coachmag.co.uk/tricep-exercises/6053/how-to-do-an-overhead-dumbbell-tricep-extension");
        exercises.add(triceps4);

        Exercise triceps5 = new Exercise();
        triceps5.setName("Underhand Triceps Pressdown");
        triceps5.setMuscleGroup(EMuscleGroup.Triceps);
        triceps5.setDurationSeconds(45);
        triceps5.setPauseDurationSeconds(90);
        triceps5.setExerciseType(EExerciseType.Isolation);
        triceps5.setExerciseLink("https://www.anabolicaliens.com/blog/reverse-grip-tricep-pushdown");
        exercises.add(triceps5);

        Exercise triceps6 = new Exercise();
        triceps6.setName("Tricep Kickbacks");
        triceps6.setMuscleGroup(EMuscleGroup.Triceps);
        triceps6.setDurationSeconds(45);
        triceps6.setPauseDurationSeconds(90);
        triceps6.setExerciseType(EExerciseType.Isolation);
        triceps6.setExerciseLink("https://www.healthline.com/health/tricep-kickbacks#how-to");
        exercises.add(triceps6);

        Exercise triceps7 = new Exercise();
        triceps7.setName("Bar Triceps Pressdown");
        triceps7.setMuscleGroup(EMuscleGroup.Triceps);
        triceps7.setDurationSeconds(45);
        triceps7.setPauseDurationSeconds(90);
        triceps7.setExerciseType(EExerciseType.Isolation);
        triceps7.setExerciseLink("https://www.coachmag.co.uk/exercises/arm-exercises/162/triceps-press-down");
        exercises.add(triceps7);

        Exercise triceps8 = new Exercise();
        triceps8.setName("Close Grip Bench Press");
        triceps8.setMuscleGroup(EMuscleGroup.Triceps);
        triceps8.setDurationSeconds(60);
        triceps8.setPauseDurationSeconds(90);
        triceps8.setExerciseType(EExerciseType.Compound);
        triceps8.setExerciseLink("https://www.coachmag.co.uk/triceps-exercises/7483/how-to-do-the-close-grip-bench-press");
        exercises.add(triceps8);

        Exercise triceps9 = new Exercise();
        triceps9.setName("Overhead Dumbbell Extensions");
        triceps9.setMuscleGroup(EMuscleGroup.Triceps);
        triceps9.setDurationSeconds(45);
        triceps9.setPauseDurationSeconds(90);
        triceps9.setExerciseType(EExerciseType.Isolation);
        triceps9.setExerciseLink("https://www.coachmag.co.uk/tricep-exercises/6053/how-to-do-an-overhead-dumbbell-tricep-extension");
        exercises.add(triceps9);

        Exercise triceps10 = new Exercise();
        triceps10.setName("Tate Press");
        triceps10.setMuscleGroup(EMuscleGroup.Triceps);
        triceps10.setDurationSeconds(45);
        triceps10.setPauseDurationSeconds(90);
        triceps10.setExerciseType(EExerciseType.Isolation);
        triceps10.setExerciseLink("https://weighttraining.guide/exercises/tate-press/");
        exercises.add(triceps10);
    }

    private static void addBicepsExercises(List<Exercise> exercises) {
        Exercise biceps1 = new Exercise();
        biceps1.setName("Dumbbell Curl");
        biceps1.setMuscleGroup(EMuscleGroup.Biceps);
        biceps1.setDurationSeconds(45);
        biceps1.setPauseDurationSeconds(90);
        biceps1.setExerciseType(EExerciseType.Isolation);
        biceps1.setExerciseLink("https://www.coachmag.co.uk/biceps-exercises/2282/bulk-up-your-biceps-with-dumbbell-biceps-curls");
        exercises.add(biceps1);

        Exercise biceps2 = new Exercise();
        biceps2.setName("Barbell Curl");
        biceps2.setMuscleGroup(EMuscleGroup.Biceps);
        biceps2.setDurationSeconds(45);
        biceps2.setPauseDurationSeconds(90);
        biceps2.setExerciseType(EExerciseType.Compound);
        biceps2.setExerciseLink("https://www.coachmag.co.uk/exercises/arm-exercises/151/barbell-curl");
        exercises.add(biceps2);

        Exercise biceps3 = new Exercise();
        biceps3.setName("Standing Cable Curl");
        biceps3.setMuscleGroup(EMuscleGroup.Biceps);
        biceps3.setDurationSeconds(45);
        biceps3.setPauseDurationSeconds(90);
        biceps3.setExerciseType(EExerciseType.Isolation);
        biceps3.setExerciseLink("https://www.verywellfit.com/how-to-do-the-biceps-cable-curl-3498289");
        exercises.add(biceps3);

        Exercise biceps4 = new Exercise();
        biceps4.setName("Hammer Curl");
        biceps4.setMuscleGroup(EMuscleGroup.Biceps);
        biceps4.setDurationSeconds(45);
        biceps4.setPauseDurationSeconds(90);
        biceps4.setExerciseType(EExerciseType.Isolation);
        biceps4.setExerciseLink("https://www.coachmag.co.uk/exercises/arm-exercises/154/hammer-curl");
        exercises.add(biceps4);

        Exercise biceps5 = new Exercise();
        biceps5.setName("Incline Dumbbell Curl");
        biceps5.setMuscleGroup(EMuscleGroup.Biceps);
        biceps5.setDurationSeconds(45);
        biceps5.setPauseDurationSeconds(90);
        biceps5.setExerciseType(EExerciseType.Isolation);
        biceps5.setExerciseLink("https://www.muscleandstrength.com/exercises/incline-dumbbell-curl.html");
        exercises.add(biceps5);

        Exercise biceps6 = new Exercise();
        biceps6.setName("Standing Reverse Barbell Curl");
        biceps6.setMuscleGroup(EMuscleGroup.Biceps);
        biceps6.setDurationSeconds(45);
        biceps6.setPauseDurationSeconds(90);
        biceps6.setExerciseType(EExerciseType.Compound);
        biceps6.setExerciseLink("https://www.coachmag.co.uk/exercises/arm-exercises/3716/the-benefits-of-reverse-curls");
        exercises.add(biceps6);

        Exercise biceps7 = new Exercise();
        biceps7.setName("Concentration Curl");
        biceps7.setMuscleGroup(EMuscleGroup.Biceps);
        biceps7.setDurationSeconds(45);
        biceps7.setPauseDurationSeconds(90);
        biceps7.setExerciseType(EExerciseType.Isolation);
        biceps7.setExerciseLink("https://www.coachmag.co.uk/biceps-exercises/8325/concentration-curl");
        exercises.add(biceps7);

        Exercise biceps8 = new Exercise();
        biceps8.setName("Cable Flex Curl");
        biceps8.setMuscleGroup(EMuscleGroup.Biceps);
        biceps8.setDurationSeconds(45);
        biceps8.setPauseDurationSeconds(90);
        biceps8.setExerciseType(EExerciseType.Isolation);
        biceps8.setExerciseLink("https://weighttraining.guide/exercises/overhead-cable-curl/");
        exercises.add(biceps8);

        Exercise biceps9 = new Exercise();
        biceps9.setName("Preacher Curl");
        biceps9.setMuscleGroup(EMuscleGroup.Biceps);
        biceps9.setDurationSeconds(45);
        biceps9.setPauseDurationSeconds(90);
        biceps9.setExerciseType(EExerciseType.Compound);
        biceps9.setExerciseLink("https://www.coachmag.co.uk/biceps-exercises/7405/how-to-do-the-preacher-curl");
        exercises.add(biceps9);

        Exercise biceps10 = new Exercise();
        biceps10.setName("Incline Dumbbell Hammer Curl");
        biceps10.setMuscleGroup(EMuscleGroup.Biceps);
        biceps10.setDurationSeconds(45);
        biceps10.setPauseDurationSeconds(90);
        biceps10.setExerciseType(EExerciseType.Isolation);
        biceps10.setExerciseLink("https://www.menshealth.com/uk/fitness/a753032/incline-hammer-curl/");
        exercises.add(biceps10);

        Exercise biceps11 = new Exercise();
        biceps11.setName("EZ-Bar Curl");
        biceps11.setMuscleGroup(EMuscleGroup.Biceps);
        biceps11.setDurationSeconds(45);
        biceps11.setPauseDurationSeconds(90);
        biceps11.setExerciseType(EExerciseType.Compound);
        biceps11.setExerciseLink("https://www.coachmag.co.uk/biceps-exercises/7123/how-to-master-the-ez-bar-curl");
        exercises.add(biceps11);

        Exercise biceps12 = new Exercise();
        biceps12.setName("Spider curl");
        biceps12.setMuscleGroup(EMuscleGroup.Biceps);
        biceps12.setDurationSeconds(45);
        biceps12.setPauseDurationSeconds(90);
        biceps12.setExerciseType(EExerciseType.Isolation);
        biceps12.setExerciseLink("https://www.coachmag.co.uk/biceps-exercises/8357/how-to-do-the-spider-curl");
        exercises.add(biceps12);
    }
}

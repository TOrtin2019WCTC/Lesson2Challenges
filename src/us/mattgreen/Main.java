package us.mattgreen;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private Scanner keyboard;
    private Cookbook cookbook;

    public Main() throws IOException {
        keyboard = new Scanner(System.in);
        cookbook = new Cookbook();

        FileInput indata = new FileInput("meals_data.csv");

        String line;

        System.out.println("Reading in meals information from file...");
        while ((line = indata.fileReadLine()) != null) {
            String[] fields = line.split(",");
            cookbook.addElementWithStrings(fields[0], fields[1], fields[2]);
        }

        runMenu();
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }

    private void printMenu() {
        System.out.println("");
        System.out.println("Select Action");
        System.out.println("1. List All Items");
        System.out.println("2. List All Items by Meal");
        System.out.println("3. Search by Meal Name");
        System.out.println("4. Do Control Break");
        System.out.println("5. Exit");
        System.out.print("Please Enter your Choice: ");
    }

    private void runMenu() throws IOException {
        boolean userContinue = true;

        while (userContinue) {
            printMenu();

            String ans = keyboard.nextLine();
            switch (ans) {
                case "1":
                    cookbook.printAllMeals();
                    break;
                case "2":
                    listByMealType();
                    break;
                case "3":
                    searchByName();
                    break;
                case "4":
                    doControlBreak();
                    break;
                case "5":
                    userContinue = false;
                    break;
            }
        }

        System.out.println("Goodbye");
        System.exit(0);
    }

    private void listByMealType() {
        // Default value pre-selected in case
        // something goes wrong w/user choice
        MealType mealType = MealType.DINNER;

        System.out.println("Which Meal Type");

        // Generate the menu using the ordinal value of the enum
        for (MealType m : MealType.values()) {
            System.out.println((m.ordinal() + 1) + ". " + m.getMeal());
        }

        System.out.print("Please Enter your Choice: ");
        String ans = keyboard.nextLine();

        try {
            int ansNum = Integer.parseInt(ans);
            if (ansNum < MealType.values().length) {
                mealType = MealType.values()[ansNum - 1];
            }
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid Meal Type " + ans + ", defaulted to " + mealType.getMeal() + ".");
        }

        cookbook.printMealsByType(mealType);
    }

    private void searchByName() {
        keyboard.nextLine();
        System.out.print("Please Enter Value: ");
        String ans = keyboard.nextLine();
        cookbook.printByNameSearch(ans);
    }
    
    private void doControlBreak() throws FileNotFoundException, IOException{
//Read in a line of the file
//Split the line into an array over the commas
//If this line contains a new meal type:
//Calculate and output the total, average, etc. for the previous type
//Empty the list (or create a new one)
//Create a new Meal object with the data
//Store the Meal object in an ArrayList
//Repeat from 1 until no more lines
//Repeat step 3a one last time for the final meal type

       FileInput indata = new FileInput("meals_data.csv");

        String line;
        String currentMealType = "nothing";
        MealType mealType;
        ArrayList<Meal> mealList = new ArrayList<>();
        int calorieTotal = 0;
        double calorieAverage = 0;
        int min = 99999;
        int max = 0;
        int median = 0;
        
        while ((line = indata.fileReadLine()) != null) {
            String[] fields = line.split(",");
         
            if(fields[0].equals(currentMealType) && mealList.size() > 0) {
                
                for (Meal m : mealList){
                    calorieTotal += m.getCalories();
                    if (m.getCalories() < min){
                        min = m.getCalories();
                    }
                    if (m.getCalories() > max)
                    max = m.getCalories();
                } 
              
                try {
                 int i = (int) mealList.size() / 2;
                  median = mealList.get(i).getCalories();
                  calorieAverage = calorieTotal/ mealList.size();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                   
                System.out.println("Meal Type\tTotal\tMean\tMin\tMax\tMedian");
                System.out.println(currentMealType + "\t " + calorieTotal + "\t " +
                        calorieAverage + "\t " + min + "\t " + max + "\t " + median);
                System.out.println();
               }
            mealList.clear();
               

            switch (fields[0]) {
                case "Breakfast":
                    mealType = MealType.BREAKFAST;
                    break;
                case "Lunch":
                    mealType = MealType.LUNCH;
                    break;
                case "Dinner":
                    mealType = MealType.DINNER;
                    break;
                case "Dessert":
                    mealType = MealType.DESSERT;
                    break;
                default:
                    mealType = MealType.DINNER;
                    //System.out.println("Meal Creation Error: Invalid Meal Type " + fields[0] + ", defaulted to Dinner.");
            }
            int calories = 0;
            try{
                calories = Integer.parseInt(fields[2]);

            }catch(NumberFormatException e){
                System.out.println(e.getMessage());
            }
            Meal meal = new Meal(mealType, fields[1], calories);
            mealList.add(meal);
            currentMealType = fields[0];

        }
        indata.fileClose();
    }
  
}

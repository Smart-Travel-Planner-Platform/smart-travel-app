Smart Travel Planner Platform


Overview

The Smart Travel Planner Platform is a Java-based desktop application developed for the SENG 324 Software Design Patterns course. It allows users to explore cities, monitor dynamic weather conditions, and hierarchically plan travel activities using multiple software design patterns.


Prerequisites
# Java Runtime Environment (JRE/JDK) installed on your machine.


How to Run the Application

This application is packaged as a "Fat JAR" containing all necessary dependencies and resources. To run the application, open your terminal or command prompt in the directory containing the JAR file and execute the following command:

# java -jar smart-travel-app-1.0-SNAPSHOT-jar-with-dependencies.jar


How to Use the Application

The Graphical User Interface (GUI) is divided into several intuitive panels:

1. Sorting & Filtering: Use the top control area to sort the city list by Name, Population, or Area. You can also filter the city list based on specific weather conditions (Sunny, Cloudy, Rainy, Snowy).

2. City Selection: Select a city from the filtered list to begin planning your trip.

3. Activity Planner: Use the middle panel to add activities (e.g., Museum Visit, Park Visit) to the selected city. You can also create custom activities with specific costs and hours.

4. Travel Plan Hierarchy: The right panel displays your travel plan as a tree structure. You can create "Composite" Activity Plan Nodes and add individual activities as "Leaf" nodes under them to build a detailed itinerary.

5. Weather Dashboard: The bottom area features dynamically updating Bar and Pie charts reflecting real-time temperature and weather distribution across all cities.

6. Undo/Redo: Made a mistake? Use the Undo and Redo buttons at the bottom right to safely revert or reapply your plan modifications.


Design Patterns Implemented

The architecture of this application strictly adheres to the requested design patterns:


Singleton: City Repository for single-point JSON data access.

Strategy: Extensible sorting algorithms for the city list.

Iterator: Filtering cities by specific weather conditions.

Observer: Dynamic live updates for the Weather Bar and Pie charts.

Decorator: Wrapping cities with extra "visit planning" features (cost and duration).

Composite: Constructing the recursive Travel Plan tree (Activity Plans and Leaves).

Command: Encapsulating user actions to support robust Undo/Redo functionality.
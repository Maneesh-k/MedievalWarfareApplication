
# Medieval Warfare Application

## Getting Started

### Build

To build the project, run the following command:

```bash
./gradlew :service:medievalware:build

```

### Run

To run the project, use the following command:

```bash
./gradlew :service:medievalware:bootRun

```

### Code Formatting

For code formatting, apply the Spotless plugin with the following command:

```bash
./gradlew spotlessApply

```

# Medieval Warfare Strategy

## Problem Statement

In the medieval world, you, as a wise king, are tasked with attacking your opponent at five locations simultaneously. Each location is defended by a platoon, consisting of soldiers from various classes. Your goal is to strategically assign your platoons to attack your opponent's platoons, aiming to win the majority of the battles.

### Platoon Rules

- Each soldier from your platoon can defeat one soldier from your opponent's platoon.
- The outcome of the battle is determined by the number of soldiers in each platoon:
  - If your platoon has 100 soldiers and your opponent's platoon has 99, you win.
  - If both platoons have the same number of soldiers, it's a draw.
  - If your platoon has 100 soldiers and your opponent's platoon has 101, you lose.

### Soldier Classes

There are six classes of soldiers, each with its own advantages over other classes:

- Militia
- Spearmen
- Light Cavalry
- Heavy Cavalry
- Foot Archer
- Cavalry Archer

### Advantage Table

| Unit Class   | Advantage Over                 |
|--------------|--------------------------------|
| Militia      | [Spearmen, LightCavalry]       |
| Spearmen     | [LightCavalry, HeavyCavalry]   |
| LightCavalry | [FootArcher, CavalryArcher]    |
| HeavyCavalry | [Militia, FootArcher, LightCavalry] |
| FootArcher   | [Spearmen, HeavyCavalry]       |
| CavalryArcher| [Militia, CavalryArcher]       |

Soldiers with an advantage over opponents can handle twice the number of opponent soldiers.

## Input

The input to the program consists of two lines:
1. Your platoons with their classes and the number of units (PlatoonClasses#NoOfSoldiers).
2. Opponent's platoons with their classes and the number of units (PlatoonClasses#NoOfSoldiers).

### Sample Input

```json
{
  "yourPlatoons": {
    "Militia": 0,
    "Spearmen": 0,
    "FootArcher": 0,
    "LightCavalry": 0,
    "HeavyCavalry": 0
  },
  "opponentPlatoons": {
    "Militia": 110,
    "Spearmen": 120,
    "FootArcher": 330,
    "LightCavalry": 450,
    "CavalryArcher": 6420
  }
}
```

### Sample Output

```json
{
  "battle": [
    {
      "ownPlatoon": "Militia",
      "opponentPlatoon": "Spearmen",
      "outcome": "win"
    },
    {
      "ownPlatoon": "Militia",
      "opponentPlatoon": "Spearmen",
      "outcome": "win"
    },
    {
      "ownPlatoon": "Militia",
      "opponentPlatoon": "Spearmen",
      "outcome": "win"
    },
    {
      "ownPlatoon": "Militia",
      "opponentPlatoon": "Spearmen",
      "outcome": "win"
    },
    {
      "ownPlatoon": "Militia",
      "opponentPlatoon": "Spearmen",
      "outcome": "win"
    }
  ]
}
```


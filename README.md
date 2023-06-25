# wizard_game
- Java 2D arcade-style game.

# How to Run
- Open terminal.
- Navigate to the gremlins_scaffold directory.
- use the command 'gradle run'.

# Controls
- Arrow keys control the wizard.
- Space bar shoots a fireball.

# Special
- Snowflake powerup freezes enemies for a period of time.
- Portal tiles will telport the player to a different random portal on the map.

# Level Configuration
- Levels designs are read from txt files specified in config.json, which also contains the cooldown for shooting fireballs and gremlin projectiles for each level, and number of lives.
- X = stone walls
- B = brick walls
- G = gremlin starting space
- W = wizard starting space
- E = exit the player must reach to progress to the next level.
- S = snowflake powerup initial spawn
- P = portal

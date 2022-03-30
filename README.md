# Minecraft pathfinding plugin

#### A program that generates random mazes and solves them with AI (and other algorithms).

#### This is an implementation of the A* pathfinding algorithm (and other algorithms).

### Commands
<code>/maze &lt;algorithm&gt; &lt;size&gt; &lt;percentage&gt;</code>

- Algorithm is required.
    - Options:
        - astar2d
        - astar3d
        - bfs2d
        - dfs2d
- Size is required.
    - Must be an integer.
- Percentage is optional.
    - Can be a decimal or integer.
    - Formats:
        - A number between 0 and 1.
        - A number between 0 and 100.
    - Both formats map to the same percentage. For example, 0.5 and 50 both equal 50%, 0.23 and 23 both equal 23%,
      etc...

### Usage
<code>/maze astar2d 30 0.5</code>
or
<code>/maze astar2d 30 50</code> (same thing)

<code>/maze astar3d 10 0.2</code>
or
<code>/maze astar3d 10 20</code> (same thing)

<code>/maze bfs2d 50 0.25</code>
or
<code>/maze bfs2d 50 25</code> (same thing)

<code>/maze dfs2d 30 0.45</code>
or
<code>/maze dfs2d 30 45</code> (same thing)

### Demo
<img src="Resources/part2.gif" width="550" height="330">

### More demos and walkthroughs
<details>
<summary><b>Initializing maze example</b></summary>
<br>
Creating a Maze and ControlPlatform object...
<br>
<br>
<code>/maze astar2d 30 50</code>
<br>
<br>
<img src="Resources/part1.gif" width="550" height="330">
</details>

<details>
<summary><b>Starting animation example</b></summary>
<br>
Watching the algorithm solve the maze...
<br>
<br>
<img src="Resources/part2.gif" width="550" height="330">
</details>

<details>
<summary><b>Changing block types example</b></summary>
<br>
Editing the block types used to create the maze and animations...
<br>
<br>
<img src="Resources/part3.gif" width="550" height="330">
</details>

<details>
<summary><b>Breadth-first search example</b></summary>
<br>
Creating a maze object utilizing BFS...
<br>
<br>
<code>/maze bfs2d 30 40</code>
<br>
<br>
<img src="Resources/part4.gif" width="550" height="330">
</details>

<details>
<summary><b>Depth-first search example</b></summary>
<br>
Creating a maze object utilizing DFS...
<br>
<br>
<code>/maze dfs2d 40 20</code>
<br>
<br>
<img src="Resources/part5.gif" width="550" height="330">
</details>

### Tech stack
- Minecraft 1.18.1
- spigot 1.18.1


### Similar repositories by <a href="https://github.com/btror/AStar">btror</a>
- <a href="https://github.com/btror/AStar">AStar</a>
- <a href="https://github.com/btror/A-Star">A-Star</a>
- <a href="https://github.com/btror/TraversalAlgorithmVisualization">TraversalAlgorithmVisualization</a>

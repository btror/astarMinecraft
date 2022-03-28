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
  - Both formats map to the same percentage. For example, 0.5 and 50 both equal 50%, 0.23 and 23 both equal 23%, etc...
  
### Example usage
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

<br>
Maze initialization
<br>
<img src="Resources/part1.gif" width="550" height="330">

A* 2D algorithm
<br>
<img src="Resources/part2.gif" width="550" height="330">
<br>

Changing block types
<br>
<img src="Resources/part3.gif" width="550" height="330">
<br>

BFS 2D algorithm
<br>
<img src="Resources/part4.gif" width="550" height="330">
<br>

DFS 2D algorithm
<br>
<img src="Resources/part5.gif" width="550" height="330">
<br>

### Similar repositories by <a href="https://github.com/btror/AStar">btror</a>
- <a href="https://github.com/btror/AStar">AStar</a>
- <a href="https://github.com/btror/A-Star">A-Star</a>
- <a href="https://github.com/btror/TraversalAlgorithmVisualization">TraversalAlgorithmVisualization</a>


### Tech stack
- Minecraft 1.18.1
- spigot 1.18.1

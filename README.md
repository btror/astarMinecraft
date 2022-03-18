# Minecraft pathfinding plugin

### A program that generates random mazes and solves them with AI.

### This is an implementation of the A* pathfinding algorithm.

<br>
2-Dimensional maze example
<br>
<img src="Resources/example2d.gif" width="550" height="330">

3-Dimensional maze example 1
<br>
<img src="Resources/example3d-1.gif" width="550" height="330">
<br>

3-Dimensional maze example 2
<br>
<img src="Resources/example3d-2.gif" width="550" height="330">
<br>

### Commands
<code>/astar2d &lt;width&gt; &lt;difficulty&gt;</code>

<code>/astar3d &lt;width&gt; &lt;difficulty&gt;</code>
- width is required and must be an integer 
- difficulty is optional (set  to medium by default)
  - easy | simple
  - medium | moderate
  - hard | difficult
  
### Example 2D usage
<code>/astar2d 45</code>
or
<code>/astar2d 45 medium</code> (same thing)

<code>/astar2d 70 easy</code>

<code>/astar2d 20 hard</code>

### Example 3D usage
<code>/astar3d 21</code>
or
<code>/astar3d 21 medium</code> (same thing)

<code>/astar3d 72 easy</code>

<code>/astar3d 65 hard</code>

### Similar repositories
- <a href="https://github.com/btror/AStar/edit/master/README.md">AStar</a> by <a href="https://github.com/btror/AStar">btror</a>


### Techs tack
- Minecraft 1.18.1
- spigot 1.18.1

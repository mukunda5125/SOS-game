<html>
<head>
<title>SOSGameGUI.java</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
.s0 { color: #cf8e6d;}
.s1 { color: #bcbec4;}
.s2 { color: #bcbec4;}
.s3 { color: #2aacb8;}
.s4 { color: #6aab73;}
</style>
</head>
<body bgcolor="#1e1f22">
<table CELLSPACING=0 CELLPADDING=5 COLS=1 WIDTH="100%" BGCOLOR="#606060" >
<tr><td><center>
<font face="Arial, Helvetica" color="#000000">
SOSGameGUI.java</font>
</center></td></tr></table>
<pre><span class="s0">package </span><span class="s1">com</span><span class="s2">.</span><span class="s1">sosgame</span><span class="s2">.</span><span class="s1">view</span><span class="s2">;</span>

<span class="s0">import </span><span class="s1">com</span><span class="s2">.</span><span class="s1">sosgame</span><span class="s2">.</span><span class="s1">model</span><span class="s2">.*;</span>
<span class="s0">import </span><span class="s1">com</span><span class="s2">.</span><span class="s1">sosgame</span><span class="s2">.</span><span class="s1">controller</span><span class="s2">.</span><span class="s1">GameController</span><span class="s2">;</span>

<span class="s0">import </span><span class="s1">javax</span><span class="s2">.</span><span class="s1">swing</span><span class="s2">.*;</span>
<span class="s0">import </span><span class="s1">java</span><span class="s2">.</span><span class="s1">awt</span><span class="s2">.*;</span>

<span class="s0">public class </span><span class="s1">SOSGameGUI </span><span class="s0">extends </span><span class="s1">JFrame </span><span class="s2">{</span>
    <span class="s0">private </span><span class="s1">GameController controller</span><span class="s2">;</span>

    <span class="s0">private </span><span class="s1">JSpinner boardSizeSpinner</span><span class="s2">;</span>
    <span class="s0">private </span><span class="s1">JRadioButton simpleModeButton</span><span class="s2">;</span>
    <span class="s0">private </span><span class="s1">JRadioButton generalModeButton</span><span class="s2">;</span>
    <span class="s0">private </span><span class="s1">JButton newGameButton</span><span class="s2">;</span>

    <span class="s0">private </span><span class="s1">JRadioButton bluePlayerS</span><span class="s2">;</span>
    <span class="s0">private </span><span class="s1">JRadioButton bluePlayerO</span><span class="s2">;</span>
    <span class="s0">private </span><span class="s1">JLabel bluePlayerLabel</span><span class="s2">;</span>

    <span class="s0">private </span><span class="s1">JRadioButton redPlayerS</span><span class="s2">;</span>
    <span class="s0">private </span><span class="s1">JRadioButton redPlayerO</span><span class="s2">;</span>
    <span class="s0">private </span><span class="s1">JLabel redPlayerLabel</span><span class="s2">;</span>

    <span class="s0">private </span><span class="s1">JPanel boardPanel</span><span class="s2">;</span>
    <span class="s0">private </span><span class="s1">JButton</span><span class="s2">[][] </span><span class="s1">boardButtons</span><span class="s2">;</span>

    <span class="s0">private </span><span class="s1">JLabel currentTurnLabel</span><span class="s2">;</span>

    <span class="s0">private static final </span><span class="s1">Color BLUE_COLOR </span><span class="s2">= </span><span class="s0">new </span><span class="s1">Color</span><span class="s2">(</span><span class="s3">51</span><span class="s2">, </span><span class="s3">153</span><span class="s2">, </span><span class="s3">255</span><span class="s2">);</span>
    <span class="s0">private static final </span><span class="s1">Color RED_COLOR </span><span class="s2">= </span><span class="s0">new </span><span class="s1">Color</span><span class="s2">(</span><span class="s3">255</span><span class="s2">, </span><span class="s3">51</span><span class="s2">, </span><span class="s3">51</span><span class="s2">);</span>

    <span class="s0">public </span><span class="s1">SOSGameGUI</span><span class="s2">() {</span>
        <span class="s1">controller </span><span class="s2">= </span><span class="s0">new </span><span class="s1">GameController</span><span class="s2">(</span><span class="s0">this</span><span class="s2">);</span>

        <span class="s1">setTitle</span><span class="s2">(</span><span class="s4">&quot;SOS Game - Sprint 2&quot;</span><span class="s2">);</span>
        <span class="s1">setDefaultCloseOperation</span><span class="s2">(</span><span class="s1">JFrame</span><span class="s2">.</span><span class="s1">EXIT_ON_CLOSE</span><span class="s2">);</span>
        <span class="s1">setLayout</span><span class="s2">(</span><span class="s0">new </span><span class="s1">BorderLayout</span><span class="s2">(</span><span class="s3">10</span><span class="s2">, </span><span class="s3">10</span><span class="s2">));</span>

        <span class="s1">initializeComponents</span><span class="s2">();</span>

        <span class="s1">setSize</span><span class="s2">(</span><span class="s3">800</span><span class="s2">, </span><span class="s3">700</span><span class="s2">);</span>
        <span class="s1">setLocationRelativeTo</span><span class="s2">(</span><span class="s0">null</span><span class="s2">);</span>
        <span class="s1">setVisible</span><span class="s2">(</span><span class="s0">true</span><span class="s2">);</span>
    <span class="s2">}</span>

    <span class="s0">private void </span><span class="s1">initializeComponents</span><span class="s2">() {</span>
        <span class="s1">add</span><span class="s2">(</span><span class="s1">createTopPanel</span><span class="s2">(), </span><span class="s1">BorderLayout</span><span class="s2">.</span><span class="s1">NORTH</span><span class="s2">);</span>
        <span class="s1">add</span><span class="s2">(</span><span class="s1">createLeftPanel</span><span class="s2">(), </span><span class="s1">BorderLayout</span><span class="s2">.</span><span class="s1">WEST</span><span class="s2">);</span>
        <span class="s1">add</span><span class="s2">(</span><span class="s1">createCenterPanel</span><span class="s2">(), </span><span class="s1">BorderLayout</span><span class="s2">.</span><span class="s1">CENTER</span><span class="s2">);</span>
        <span class="s1">add</span><span class="s2">(</span><span class="s1">createRightPanel</span><span class="s2">(), </span><span class="s1">BorderLayout</span><span class="s2">.</span><span class="s1">EAST</span><span class="s2">);</span>
        <span class="s1">add</span><span class="s2">(</span><span class="s1">createBottomPanel</span><span class="s2">(), </span><span class="s1">BorderLayout</span><span class="s2">.</span><span class="s1">SOUTH</span><span class="s2">);</span>
    <span class="s2">}</span>

    <span class="s0">private </span><span class="s1">JPanel createTopPanel</span><span class="s2">() {</span>
        <span class="s1">JPanel topPanel </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JPanel</span><span class="s2">(</span><span class="s0">new </span><span class="s1">FlowLayout</span><span class="s2">(</span><span class="s1">FlowLayout</span><span class="s2">.</span><span class="s1">CENTER</span><span class="s2">, </span><span class="s3">20</span><span class="s2">, </span><span class="s3">10</span><span class="s2">));</span>
        <span class="s1">topPanel</span><span class="s2">.</span><span class="s1">setBorder</span><span class="s2">(</span><span class="s1">BorderFactory</span><span class="s2">.</span><span class="s1">createEtchedBorder</span><span class="s2">());</span>

        <span class="s1">JLabel sosLabel </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JLabel</span><span class="s2">(</span><span class="s4">&quot;SOS&quot;</span><span class="s2">);</span>
        <span class="s1">sosLabel</span><span class="s2">.</span><span class="s1">setFont</span><span class="s2">(</span><span class="s0">new </span><span class="s1">Font</span><span class="s2">(</span><span class="s4">&quot;Arial&quot;</span><span class="s2">, </span><span class="s1">Font</span><span class="s2">.</span><span class="s1">BOLD</span><span class="s2">, </span><span class="s3">24</span><span class="s2">));</span>
        <span class="s1">topPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">sosLabel</span><span class="s2">);</span>

        <span class="s1">simpleModeButton </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JRadioButton</span><span class="s2">(</span><span class="s4">&quot;Simple game&quot;</span><span class="s2">, </span><span class="s0">true</span><span class="s2">);</span>
        <span class="s1">generalModeButton </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JRadioButton</span><span class="s2">(</span><span class="s4">&quot;General game&quot;</span><span class="s2">);</span>

        <span class="s1">ButtonGroup modeGroup </span><span class="s2">= </span><span class="s0">new </span><span class="s1">ButtonGroup</span><span class="s2">();</span>
        <span class="s1">modeGroup</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">simpleModeButton</span><span class="s2">);</span>
        <span class="s1">modeGroup</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">generalModeButton</span><span class="s2">);</span>

        <span class="s1">simpleModeButton</span><span class="s2">.</span><span class="s1">addActionListener</span><span class="s2">(</span><span class="s1">e -&gt; controller</span><span class="s2">.</span><span class="s1">setGameMode</span><span class="s2">(</span><span class="s1">GameMode</span><span class="s2">.</span><span class="s1">SIMPLE</span><span class="s2">));</span>
        <span class="s1">generalModeButton</span><span class="s2">.</span><span class="s1">addActionListener</span><span class="s2">(</span><span class="s1">e -&gt; controller</span><span class="s2">.</span><span class="s1">setGameMode</span><span class="s2">(</span><span class="s1">GameMode</span><span class="s2">.</span><span class="s1">GENERAL</span><span class="s2">));</span>

        <span class="s1">topPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">simpleModeButton</span><span class="s2">);</span>
        <span class="s1">topPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">generalModeButton</span><span class="s2">);</span>

        <span class="s1">topPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s0">new </span><span class="s1">JLabel</span><span class="s2">(</span><span class="s4">&quot;Board size:&quot;</span><span class="s2">));</span>
        <span class="s1">SpinnerModel sizeModel </span><span class="s2">= </span><span class="s0">new </span><span class="s1">SpinnerNumberModel</span><span class="s2">(</span><span class="s3">8</span><span class="s2">, </span><span class="s3">3</span><span class="s2">, </span><span class="s3">20</span><span class="s2">, </span><span class="s3">1</span><span class="s2">);</span>
        <span class="s1">boardSizeSpinner </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JSpinner</span><span class="s2">(</span><span class="s1">sizeModel</span><span class="s2">);</span>
        <span class="s1">boardSizeSpinner</span><span class="s2">.</span><span class="s1">addChangeListener</span><span class="s2">(</span><span class="s1">e -&gt; </span><span class="s2">{</span>
            <span class="s0">int </span><span class="s1">size </span><span class="s2">= (</span><span class="s1">Integer</span><span class="s2">) </span><span class="s1">boardSizeSpinner</span><span class="s2">.</span><span class="s1">getValue</span><span class="s2">();</span>
            <span class="s1">controller</span><span class="s2">.</span><span class="s1">setBoardSize</span><span class="s2">(</span><span class="s1">size</span><span class="s2">);</span>
        <span class="s2">});</span>
        <span class="s1">topPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">boardSizeSpinner</span><span class="s2">);</span>

        <span class="s1">newGameButton </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JButton</span><span class="s2">(</span><span class="s4">&quot;New Game&quot;</span><span class="s2">);</span>
        <span class="s1">newGameButton</span><span class="s2">.</span><span class="s1">addActionListener</span><span class="s2">(</span><span class="s1">e -&gt; controller</span><span class="s2">.</span><span class="s1">startNewGame</span><span class="s2">());</span>
        <span class="s1">topPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">newGameButton</span><span class="s2">);</span>

        <span class="s0">return </span><span class="s1">topPanel</span><span class="s2">;</span>
    <span class="s2">}</span>

    <span class="s0">private </span><span class="s1">JPanel createLeftPanel</span><span class="s2">() {</span>
        <span class="s1">JPanel leftPanel </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JPanel</span><span class="s2">();</span>
        <span class="s1">leftPanel</span><span class="s2">.</span><span class="s1">setLayout</span><span class="s2">(</span><span class="s0">new </span><span class="s1">BoxLayout</span><span class="s2">(</span><span class="s1">leftPanel</span><span class="s2">, </span><span class="s1">BoxLayout</span><span class="s2">.</span><span class="s1">Y_AXIS</span><span class="s2">));</span>
        <span class="s1">leftPanel</span><span class="s2">.</span><span class="s1">setBorder</span><span class="s2">(</span><span class="s1">BorderFactory</span><span class="s2">.</span><span class="s1">createEmptyBorder</span><span class="s2">(</span><span class="s3">10</span><span class="s2">, </span><span class="s3">10</span><span class="s2">, </span><span class="s3">10</span><span class="s2">, </span><span class="s3">10</span><span class="s2">));</span>

        <span class="s1">bluePlayerLabel </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JLabel</span><span class="s2">(</span><span class="s4">&quot;Blue player&quot;</span><span class="s2">);</span>
        <span class="s1">bluePlayerLabel</span><span class="s2">.</span><span class="s1">setForeground</span><span class="s2">(</span><span class="s1">BLUE_COLOR</span><span class="s2">);</span>
        <span class="s1">bluePlayerLabel</span><span class="s2">.</span><span class="s1">setFont</span><span class="s2">(</span><span class="s0">new </span><span class="s1">Font</span><span class="s2">(</span><span class="s4">&quot;Arial&quot;</span><span class="s2">, </span><span class="s1">Font</span><span class="s2">.</span><span class="s1">BOLD</span><span class="s2">, </span><span class="s3">16</span><span class="s2">));</span>
        <span class="s1">bluePlayerLabel</span><span class="s2">.</span><span class="s1">setAlignmentX</span><span class="s2">(</span><span class="s1">Component</span><span class="s2">.</span><span class="s1">CENTER_ALIGNMENT</span><span class="s2">);</span>

        <span class="s1">bluePlayerS </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JRadioButton</span><span class="s2">(</span><span class="s4">&quot;S&quot;</span><span class="s2">, </span><span class="s0">true</span><span class="s2">);</span>
        <span class="s1">bluePlayerO </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JRadioButton</span><span class="s2">(</span><span class="s4">&quot;O&quot;</span><span class="s2">);</span>

        <span class="s1">ButtonGroup blueGroup </span><span class="s2">= </span><span class="s0">new </span><span class="s1">ButtonGroup</span><span class="s2">();</span>
        <span class="s1">blueGroup</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">bluePlayerS</span><span class="s2">);</span>
        <span class="s1">blueGroup</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">bluePlayerO</span><span class="s2">);</span>

        <span class="s1">leftPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">bluePlayerLabel</span><span class="s2">);</span>
        <span class="s1">leftPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">Box</span><span class="s2">.</span><span class="s1">createRigidArea</span><span class="s2">(</span><span class="s0">new </span><span class="s1">Dimension</span><span class="s2">(</span><span class="s3">0</span><span class="s2">, </span><span class="s3">20</span><span class="s2">)));</span>
        <span class="s1">leftPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">bluePlayerS</span><span class="s2">);</span>
        <span class="s1">leftPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">bluePlayerO</span><span class="s2">);</span>

        <span class="s0">return </span><span class="s1">leftPanel</span><span class="s2">;</span>
    <span class="s2">}</span>

    <span class="s0">private </span><span class="s1">JPanel createRightPanel</span><span class="s2">() {</span>
        <span class="s1">JPanel rightPanel </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JPanel</span><span class="s2">();</span>
        <span class="s1">rightPanel</span><span class="s2">.</span><span class="s1">setLayout</span><span class="s2">(</span><span class="s0">new </span><span class="s1">BoxLayout</span><span class="s2">(</span><span class="s1">rightPanel</span><span class="s2">, </span><span class="s1">BoxLayout</span><span class="s2">.</span><span class="s1">Y_AXIS</span><span class="s2">));</span>
        <span class="s1">rightPanel</span><span class="s2">.</span><span class="s1">setBorder</span><span class="s2">(</span><span class="s1">BorderFactory</span><span class="s2">.</span><span class="s1">createEmptyBorder</span><span class="s2">(</span><span class="s3">10</span><span class="s2">, </span><span class="s3">10</span><span class="s2">, </span><span class="s3">10</span><span class="s2">, </span><span class="s3">10</span><span class="s2">));</span>

        <span class="s1">redPlayerLabel </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JLabel</span><span class="s2">(</span><span class="s4">&quot;Red player&quot;</span><span class="s2">);</span>
        <span class="s1">redPlayerLabel</span><span class="s2">.</span><span class="s1">setForeground</span><span class="s2">(</span><span class="s1">RED_COLOR</span><span class="s2">);</span>
        <span class="s1">redPlayerLabel</span><span class="s2">.</span><span class="s1">setFont</span><span class="s2">(</span><span class="s0">new </span><span class="s1">Font</span><span class="s2">(</span><span class="s4">&quot;Arial&quot;</span><span class="s2">, </span><span class="s1">Font</span><span class="s2">.</span><span class="s1">BOLD</span><span class="s2">, </span><span class="s3">16</span><span class="s2">));</span>
        <span class="s1">redPlayerLabel</span><span class="s2">.</span><span class="s1">setAlignmentX</span><span class="s2">(</span><span class="s1">Component</span><span class="s2">.</span><span class="s1">CENTER_ALIGNMENT</span><span class="s2">);</span>

        <span class="s1">redPlayerS </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JRadioButton</span><span class="s2">(</span><span class="s4">&quot;S&quot;</span><span class="s2">, </span><span class="s0">true</span><span class="s2">);</span>
        <span class="s1">redPlayerO </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JRadioButton</span><span class="s2">(</span><span class="s4">&quot;O&quot;</span><span class="s2">);</span>

        <span class="s1">ButtonGroup redGroup </span><span class="s2">= </span><span class="s0">new </span><span class="s1">ButtonGroup</span><span class="s2">();</span>
        <span class="s1">redGroup</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">redPlayerS</span><span class="s2">);</span>
        <span class="s1">redGroup</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">redPlayerO</span><span class="s2">);</span>

        <span class="s1">rightPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">redPlayerLabel</span><span class="s2">);</span>
        <span class="s1">rightPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">Box</span><span class="s2">.</span><span class="s1">createRigidArea</span><span class="s2">(</span><span class="s0">new </span><span class="s1">Dimension</span><span class="s2">(</span><span class="s3">0</span><span class="s2">, </span><span class="s3">20</span><span class="s2">)));</span>
        <span class="s1">rightPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">redPlayerS</span><span class="s2">);</span>
        <span class="s1">rightPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">redPlayerO</span><span class="s2">);</span>

        <span class="s0">return </span><span class="s1">rightPanel</span><span class="s2">;</span>
    <span class="s2">}</span>

    <span class="s0">private </span><span class="s1">JPanel createCenterPanel</span><span class="s2">() {</span>
        <span class="s1">boardPanel </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JPanel</span><span class="s2">();</span>
        <span class="s1">createBoard</span><span class="s2">(</span><span class="s3">8</span><span class="s2">);</span>
        <span class="s0">return </span><span class="s1">boardPanel</span><span class="s2">;</span>
    <span class="s2">}</span>

    <span class="s0">public void </span><span class="s1">createBoard</span><span class="s2">(</span><span class="s0">int </span><span class="s1">size</span><span class="s2">) {</span>
        <span class="s1">boardPanel</span><span class="s2">.</span><span class="s1">removeAll</span><span class="s2">();</span>
        <span class="s1">boardPanel</span><span class="s2">.</span><span class="s1">setLayout</span><span class="s2">(</span><span class="s0">new </span><span class="s1">GridLayout</span><span class="s2">(</span><span class="s1">size</span><span class="s2">, </span><span class="s1">size</span><span class="s2">, </span><span class="s3">2</span><span class="s2">, </span><span class="s3">2</span><span class="s2">));</span>
        <span class="s1">boardPanel</span><span class="s2">.</span><span class="s1">setBorder</span><span class="s2">(</span><span class="s1">BorderFactory</span><span class="s2">.</span><span class="s1">createEmptyBorder</span><span class="s2">(</span><span class="s3">10</span><span class="s2">, </span><span class="s3">10</span><span class="s2">, </span><span class="s3">10</span><span class="s2">, </span><span class="s3">10</span><span class="s2">));</span>

        <span class="s1">boardButtons </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JButton</span><span class="s2">[</span><span class="s1">size</span><span class="s2">][</span><span class="s1">size</span><span class="s2">];</span>

        <span class="s0">for </span><span class="s2">(</span><span class="s0">int </span><span class="s1">i </span><span class="s2">= </span><span class="s3">0</span><span class="s2">; </span><span class="s1">i </span><span class="s2">&lt; </span><span class="s1">size</span><span class="s2">; </span><span class="s1">i</span><span class="s2">++) {</span>
            <span class="s0">for </span><span class="s2">(</span><span class="s0">int </span><span class="s1">j </span><span class="s2">= </span><span class="s3">0</span><span class="s2">; </span><span class="s1">j </span><span class="s2">&lt; </span><span class="s1">size</span><span class="s2">; </span><span class="s1">j</span><span class="s2">++) {</span>
                <span class="s1">JButton button </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JButton</span><span class="s2">(</span><span class="s4">&quot;&quot;</span><span class="s2">);</span>
                <span class="s1">button</span><span class="s2">.</span><span class="s1">setFont</span><span class="s2">(</span><span class="s0">new </span><span class="s1">Font</span><span class="s2">(</span><span class="s4">&quot;Arial&quot;</span><span class="s2">, </span><span class="s1">Font</span><span class="s2">.</span><span class="s1">BOLD</span><span class="s2">, </span><span class="s3">24</span><span class="s2">));</span>
                <span class="s1">button</span><span class="s2">.</span><span class="s1">setFocusPainted</span><span class="s2">(</span><span class="s0">false</span><span class="s2">);</span>
                <span class="s1">button</span><span class="s2">.</span><span class="s1">setBackground</span><span class="s2">(</span><span class="s1">Color</span><span class="s2">.</span><span class="s1">WHITE</span><span class="s2">);</span>

                <span class="s0">final int </span><span class="s1">row </span><span class="s2">= </span><span class="s1">i</span><span class="s2">;</span>
                <span class="s0">final int </span><span class="s1">col </span><span class="s2">= </span><span class="s1">j</span><span class="s2">;</span>

                <span class="s1">button</span><span class="s2">.</span><span class="s1">addActionListener</span><span class="s2">(</span><span class="s1">e -&gt; controller</span><span class="s2">.</span><span class="s1">handleCellClick</span><span class="s2">(</span><span class="s1">row</span><span class="s2">, </span><span class="s1">col</span><span class="s2">));</span>

                <span class="s1">boardButtons</span><span class="s2">[</span><span class="s1">i</span><span class="s2">][</span><span class="s1">j</span><span class="s2">] = </span><span class="s1">button</span><span class="s2">;</span>
                <span class="s1">boardPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">button</span><span class="s2">);</span>
            <span class="s2">}</span>
        <span class="s2">}</span>

        <span class="s1">boardPanel</span><span class="s2">.</span><span class="s1">revalidate</span><span class="s2">();</span>
        <span class="s1">boardPanel</span><span class="s2">.</span><span class="s1">repaint</span><span class="s2">();</span>
    <span class="s2">}</span>

    <span class="s0">private </span><span class="s1">JPanel createBottomPanel</span><span class="s2">() {</span>
        <span class="s1">JPanel bottomPanel </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JPanel</span><span class="s2">(</span><span class="s0">new </span><span class="s1">FlowLayout</span><span class="s2">(</span><span class="s1">FlowLayout</span><span class="s2">.</span><span class="s1">CENTER</span><span class="s2">));</span>
        <span class="s1">bottomPanel</span><span class="s2">.</span><span class="s1">setBorder</span><span class="s2">(</span><span class="s1">BorderFactory</span><span class="s2">.</span><span class="s1">createEtchedBorder</span><span class="s2">());</span>

        <span class="s1">currentTurnLabel </span><span class="s2">= </span><span class="s0">new </span><span class="s1">JLabel</span><span class="s2">(</span><span class="s4">&quot;Current turn: Blue&quot;</span><span class="s2">);</span>
        <span class="s1">currentTurnLabel</span><span class="s2">.</span><span class="s1">setFont</span><span class="s2">(</span><span class="s0">new </span><span class="s1">Font</span><span class="s2">(</span><span class="s4">&quot;Arial&quot;</span><span class="s2">, </span><span class="s1">Font</span><span class="s2">.</span><span class="s1">BOLD</span><span class="s2">, </span><span class="s3">16</span><span class="s2">));</span>
        <span class="s1">currentTurnLabel</span><span class="s2">.</span><span class="s1">setForeground</span><span class="s2">(</span><span class="s1">BLUE_COLOR</span><span class="s2">);</span>

        <span class="s1">bottomPanel</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">currentTurnLabel</span><span class="s2">);</span>

        <span class="s0">return </span><span class="s1">bottomPanel</span><span class="s2">;</span>
    <span class="s2">}</span>

    <span class="s0">public void </span><span class="s1">updateCell</span><span class="s2">(</span><span class="s0">int </span><span class="s1">row</span><span class="s2">, </span><span class="s0">int </span><span class="s1">col</span><span class="s2">, </span><span class="s0">char </span><span class="s1">letter</span><span class="s2">, </span><span class="s1">Player player</span><span class="s2">) {</span>
        <span class="s1">boardButtons</span><span class="s2">[</span><span class="s1">row</span><span class="s2">][</span><span class="s1">col</span><span class="s2">].</span><span class="s1">setText</span><span class="s2">(</span><span class="s1">String</span><span class="s2">.</span><span class="s1">valueOf</span><span class="s2">(</span><span class="s1">letter</span><span class="s2">));</span>

        <span class="s0">if </span><span class="s2">(</span><span class="s1">player </span><span class="s2">== </span><span class="s1">Player</span><span class="s2">.</span><span class="s1">BLUE</span><span class="s2">) {</span>
            <span class="s1">boardButtons</span><span class="s2">[</span><span class="s1">row</span><span class="s2">][</span><span class="s1">col</span><span class="s2">].</span><span class="s1">setForeground</span><span class="s2">(</span><span class="s1">BLUE_COLOR</span><span class="s2">);</span>
        <span class="s2">} </span><span class="s0">else </span><span class="s2">{</span>
            <span class="s1">boardButtons</span><span class="s2">[</span><span class="s1">row</span><span class="s2">][</span><span class="s1">col</span><span class="s2">].</span><span class="s1">setForeground</span><span class="s2">(</span><span class="s1">RED_COLOR</span><span class="s2">);</span>
        <span class="s2">}</span>
    <span class="s2">}</span>

    <span class="s0">public void </span><span class="s1">updateTurnDisplay</span><span class="s2">(</span><span class="s1">Player player</span><span class="s2">) {</span>
        <span class="s0">if </span><span class="s2">(</span><span class="s1">player </span><span class="s2">== </span><span class="s1">Player</span><span class="s2">.</span><span class="s1">BLUE</span><span class="s2">) {</span>
            <span class="s1">currentTurnLabel</span><span class="s2">.</span><span class="s1">setText</span><span class="s2">(</span><span class="s4">&quot;Current turn: Blue&quot;</span><span class="s2">);</span>
            <span class="s1">currentTurnLabel</span><span class="s2">.</span><span class="s1">setForeground</span><span class="s2">(</span><span class="s1">BLUE_COLOR</span><span class="s2">);</span>
        <span class="s2">} </span><span class="s0">else </span><span class="s2">{</span>
            <span class="s1">currentTurnLabel</span><span class="s2">.</span><span class="s1">setText</span><span class="s2">(</span><span class="s4">&quot;Current turn: Red&quot;</span><span class="s2">);</span>
            <span class="s1">currentTurnLabel</span><span class="s2">.</span><span class="s1">setForeground</span><span class="s2">(</span><span class="s1">RED_COLOR</span><span class="s2">);</span>
        <span class="s2">}</span>
    <span class="s2">}</span>

    <span class="s0">public char </span><span class="s1">getSelectedLetter</span><span class="s2">(</span><span class="s1">Player player</span><span class="s2">) {</span>
        <span class="s0">if </span><span class="s2">(</span><span class="s1">player </span><span class="s2">== </span><span class="s1">Player</span><span class="s2">.</span><span class="s1">BLUE</span><span class="s2">) {</span>
            <span class="s0">return </span><span class="s1">bluePlayerS</span><span class="s2">.</span><span class="s1">isSelected</span><span class="s2">() ? </span><span class="s4">'S' </span><span class="s2">: </span><span class="s4">'O'</span><span class="s2">;</span>
        <span class="s2">} </span><span class="s0">else </span><span class="s2">{</span>
            <span class="s0">return </span><span class="s1">redPlayerS</span><span class="s2">.</span><span class="s1">isSelected</span><span class="s2">() ? </span><span class="s4">'S' </span><span class="s2">: </span><span class="s4">'O'</span><span class="s2">;</span>
        <span class="s2">}</span>
    <span class="s2">}</span>

    <span class="s0">public void </span><span class="s1">resetBoardDisplay</span><span class="s2">() {</span>
        <span class="s0">for </span><span class="s2">(</span><span class="s0">int </span><span class="s1">i </span><span class="s2">= </span><span class="s3">0</span><span class="s2">; </span><span class="s1">i </span><span class="s2">&lt; </span><span class="s1">boardButtons</span><span class="s2">.</span><span class="s1">length</span><span class="s2">; </span><span class="s1">i</span><span class="s2">++) {</span>
            <span class="s0">for </span><span class="s2">(</span><span class="s0">int </span><span class="s1">j </span><span class="s2">= </span><span class="s3">0</span><span class="s2">; </span><span class="s1">j </span><span class="s2">&lt; </span><span class="s1">boardButtons</span><span class="s2">[</span><span class="s1">i</span><span class="s2">].</span><span class="s1">length</span><span class="s2">; </span><span class="s1">j</span><span class="s2">++) {</span>
                <span class="s1">boardButtons</span><span class="s2">[</span><span class="s1">i</span><span class="s2">][</span><span class="s1">j</span><span class="s2">].</span><span class="s1">setText</span><span class="s2">(</span><span class="s4">&quot;&quot;</span><span class="s2">);</span>
                <span class="s1">boardButtons</span><span class="s2">[</span><span class="s1">i</span><span class="s2">][</span><span class="s1">j</span><span class="s2">].</span><span class="s1">setBackground</span><span class="s2">(</span><span class="s1">Color</span><span class="s2">.</span><span class="s1">WHITE</span><span class="s2">);</span>
            <span class="s2">}</span>
        <span class="s2">}</span>
    <span class="s2">}</span>

    <span class="s0">public int </span><span class="s1">getBoardSizeFromSpinner</span><span class="s2">() {</span>
        <span class="s0">return </span><span class="s2">(</span><span class="s1">Integer</span><span class="s2">) </span><span class="s1">boardSizeSpinner</span><span class="s2">.</span><span class="s1">getValue</span><span class="s2">();</span>
    <span class="s2">}</span>

    <span class="s0">public </span><span class="s1">GameMode getSelectedGameMode</span><span class="s2">() {</span>
        <span class="s0">return </span><span class="s1">simpleModeButton</span><span class="s2">.</span><span class="s1">isSelected</span><span class="s2">() ? </span><span class="s1">GameMode</span><span class="s2">.</span><span class="s1">SIMPLE </span><span class="s2">: </span><span class="s1">GameMode</span><span class="s2">.</span><span class="s1">GENERAL</span><span class="s2">;</span>
    <span class="s2">}</span>

    <span class="s0">public static void </span><span class="s1">main</span><span class="s2">(</span><span class="s1">String</span><span class="s2">[] </span><span class="s1">args</span><span class="s2">) {</span>
        <span class="s1">SwingUtilities</span><span class="s2">.</span><span class="s1">invokeLater</span><span class="s2">(() </span><span class="s1">-&gt; </span><span class="s0">new </span><span class="s1">SOSGameGUI</span><span class="s2">());</span>
    <span class="s2">}</span>
<span class="s2">}</span></pre>
</body>
</html>
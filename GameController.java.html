<html>
<head>
<title>GameController.java</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
.s0 { color: #cf8e6d;}
.s1 { color: #bcbec4;}
.s2 { color: #bcbec4;}
</style>
</head>
<body bgcolor="#1e1f22">
<table CELLSPACING=0 CELLPADDING=5 COLS=1 WIDTH="100%" BGCOLOR="#606060" >
<tr><td><center>
<font face="Arial, Helvetica" color="#000000">
GameController.java</font>
</center></td></tr></table>
<pre><span class="s0">package </span><span class="s1">com</span><span class="s2">.</span><span class="s1">sosgame</span><span class="s2">.</span><span class="s1">controller</span><span class="s2">;</span>

<span class="s0">import </span><span class="s1">com</span><span class="s2">.</span><span class="s1">sosgame</span><span class="s2">.</span><span class="s1">model</span><span class="s2">.*;</span>
<span class="s0">import </span><span class="s1">com</span><span class="s2">.</span><span class="s1">sosgame</span><span class="s2">.</span><span class="s1">view</span><span class="s2">.</span><span class="s1">SOSGameGUI</span><span class="s2">;</span>

<span class="s0">public class </span><span class="s1">GameController </span><span class="s2">{</span>
    <span class="s0">private </span><span class="s1">SOSGame game</span><span class="s2">;</span>
    <span class="s0">private </span><span class="s1">SOSGameGUI gui</span><span class="s2">;</span>

    <span class="s0">public </span><span class="s1">GameController</span><span class="s2">(</span><span class="s1">SOSGameGUI gui</span><span class="s2">) {</span>
        <span class="s0">this</span><span class="s2">.</span><span class="s1">gui </span><span class="s2">= </span><span class="s1">gui</span><span class="s2">;</span>
        <span class="s0">this</span><span class="s2">.</span><span class="s1">game </span><span class="s2">= </span><span class="s0">new </span><span class="s1">SOSGame</span><span class="s2">();</span>
    <span class="s2">}</span>

    <span class="s0">public void </span><span class="s1">handleCellClick</span><span class="s2">(</span><span class="s0">int </span><span class="s1">row</span><span class="s2">, </span><span class="s0">int </span><span class="s1">col</span><span class="s2">) {</span>
        <span class="s1">Player currentPlayer </span><span class="s2">= </span><span class="s1">game</span><span class="s2">.</span><span class="s1">getCurrentPlayer</span><span class="s2">();</span>
        <span class="s0">char </span><span class="s1">selectedLetter </span><span class="s2">= </span><span class="s1">gui</span><span class="s2">.</span><span class="s1">getSelectedLetter</span><span class="s2">(</span><span class="s1">currentPlayer</span><span class="s2">);</span>

        <span class="s0">boolean </span><span class="s1">success </span><span class="s2">= </span><span class="s1">game</span><span class="s2">.</span><span class="s1">makeMove</span><span class="s2">(</span><span class="s1">row</span><span class="s2">, </span><span class="s1">col</span><span class="s2">, </span><span class="s1">selectedLetter</span><span class="s2">);</span>

        <span class="s0">if </span><span class="s2">(</span><span class="s1">success</span><span class="s2">) {</span>
            <span class="s1">gui</span><span class="s2">.</span><span class="s1">updateCell</span><span class="s2">(</span><span class="s1">row</span><span class="s2">, </span><span class="s1">col</span><span class="s2">, </span><span class="s1">selectedLetter</span><span class="s2">, </span><span class="s1">currentPlayer</span><span class="s2">);</span>
            <span class="s1">gui</span><span class="s2">.</span><span class="s1">updateTurnDisplay</span><span class="s2">(</span><span class="s1">game</span><span class="s2">.</span><span class="s1">getCurrentPlayer</span><span class="s2">());</span>
        <span class="s2">}</span>
    <span class="s2">}</span>

    <span class="s0">public void </span><span class="s1">startNewGame</span><span class="s2">() {</span>
        <span class="s0">int </span><span class="s1">size </span><span class="s2">= </span><span class="s1">gui</span><span class="s2">.</span><span class="s1">getBoardSizeFromSpinner</span><span class="s2">();</span>
        <span class="s1">GameMode mode </span><span class="s2">= </span><span class="s1">gui</span><span class="s2">.</span><span class="s1">getSelectedGameMode</span><span class="s2">();</span>

        <span class="s1">game</span><span class="s2">.</span><span class="s1">startNewGame</span><span class="s2">(</span><span class="s1">size</span><span class="s2">, </span><span class="s1">mode</span><span class="s2">);</span>
        <span class="s1">gui</span><span class="s2">.</span><span class="s1">createBoard</span><span class="s2">(</span><span class="s1">size</span><span class="s2">);</span>
        <span class="s1">gui</span><span class="s2">.</span><span class="s1">resetBoardDisplay</span><span class="s2">();</span>
        <span class="s1">gui</span><span class="s2">.</span><span class="s1">updateTurnDisplay</span><span class="s2">(</span><span class="s1">game</span><span class="s2">.</span><span class="s1">getCurrentPlayer</span><span class="s2">());</span>
    <span class="s2">}</span>

    <span class="s0">public void </span><span class="s1">setBoardSize</span><span class="s2">(</span><span class="s0">int </span><span class="s1">size</span><span class="s2">) {</span>
        <span class="s1">game</span><span class="s2">.</span><span class="s1">setBoardSize</span><span class="s2">(</span><span class="s1">size</span><span class="s2">);</span>
    <span class="s2">}</span>

    <span class="s0">public void </span><span class="s1">setGameMode</span><span class="s2">(</span><span class="s1">GameMode mode</span><span class="s2">) {</span>
        <span class="s1">game</span><span class="s2">.</span><span class="s1">setGameMode</span><span class="s2">(</span><span class="s1">mode</span><span class="s2">);</span>
    <span class="s2">}</span>

    <span class="s0">public </span><span class="s1">SOSGame getGame</span><span class="s2">() {</span>
        <span class="s0">return </span><span class="s1">game</span><span class="s2">;</span>
    <span class="s2">}</span>
<span class="s2">}</span></pre>
</body>
</html>
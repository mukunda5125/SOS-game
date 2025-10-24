<html>
<head>
<title>Player.java</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
.s0 { color: #cf8e6d;}
.s1 { color: #bcbec4;}
.s2 { color: #bcbec4;}
.s3 { color: #6aab73;}
</style>
</head>
<body bgcolor="#1e1f22">
<table CELLSPACING=0 CELLPADDING=5 COLS=1 WIDTH="100%" BGCOLOR="#606060" >
<tr><td><center>
<font face="Arial, Helvetica" color="#000000">
Player.java</font>
</center></td></tr></table>
<pre><span class="s0">package </span><span class="s1">com</span><span class="s2">.</span><span class="s1">sosgame</span><span class="s2">.</span><span class="s1">model</span><span class="s2">;</span>

<span class="s0">public enum </span><span class="s1">Player </span><span class="s2">{</span>
    <span class="s1">BLUE</span><span class="s2">,</span>
    <span class="s1">RED</span><span class="s2">;</span>

    <span class="s0">private char </span><span class="s1">selectedLetter </span><span class="s2">= </span><span class="s3">'S'</span><span class="s2">;</span>

    <span class="s0">public char </span><span class="s1">getSelectedLetter</span><span class="s2">() {</span>
        <span class="s0">return </span><span class="s1">selectedLetter</span><span class="s2">;</span>
    <span class="s2">}</span>

    <span class="s0">public void </span><span class="s1">setSelectedLetter</span><span class="s2">(</span><span class="s0">char </span><span class="s1">letter</span><span class="s2">) {</span>
        <span class="s0">if </span><span class="s2">(</span><span class="s1">letter </span><span class="s2">== </span><span class="s3">'S' </span><span class="s2">|| </span><span class="s1">letter </span><span class="s2">== </span><span class="s3">'O'</span><span class="s2">) {</span>
            <span class="s0">this</span><span class="s2">.</span><span class="s1">selectedLetter </span><span class="s2">= </span><span class="s1">letter</span><span class="s2">;</span>
        <span class="s2">}</span>
    <span class="s2">}</span>

    <span class="s0">public </span><span class="s1">Player getOpponent</span><span class="s2">() {</span>
        <span class="s0">return this </span><span class="s2">== </span><span class="s1">BLUE </span><span class="s2">? </span><span class="s1">RED </span><span class="s2">: </span><span class="s1">BLUE</span><span class="s2">;</span>
    <span class="s2">}</span>
<span class="s2">}</span></pre>
</body>
</html>
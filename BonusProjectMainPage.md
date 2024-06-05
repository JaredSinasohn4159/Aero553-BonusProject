@mainpage
<br />

# Aero553-BonusProject

## Problem Statement
“Write a computer program using any compiled language, such as C/C++, Java, FORTRAN, etc. ( not interpreted language such as Matlab, Python, Octave, Scilab, etc.) That takes in two inputs x and y and returns a value that is x raised to the power y. The inputs x and y can be any valid real numbers.”

## The Math
Start with an equation for the problem definition
$$a = x^y$$

$\text{Let } y = N + d \text{ where } \lbrace N \in \mathbb{Z} \rbrace \text{ and } 0 \leq d \leq 1$\

This results in
$$x^y = x^Nx^d$$
$x^N$ can be solved simply with a for loop.

$x^d$ is not so simple.
We can use the power of exponents and logarithms to allow for solving of $x^d$
$$x^d = e^{d \ln(x)}$$
As long as $x \neq 0$ (which we can check for) before performing computations 

This solves for almost all situations, except for when $d \neq 0$ and $x < 0$
For this, we need to look to De Moivre.

$$De Moivre's Theorem$$
$$x^n = |x|^n(\cos(\pi n) + i \sin(\pi n))$$

We can use this with x^d, making the full equation
$$x^y = x^Ne^{d \ln(x)}(\cos(\pi d) + i \sin(\pi d))$$

Now we can perform the implementation, which was done in Java.  All functions used to perform this computation can be found in the **Exponentiator.java** class

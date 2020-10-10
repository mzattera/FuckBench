/*
Below this comment there are 760 bytes (including spaces and newlines)
of purposely obfuscated C source code, that form a program able to play
chess, including also a little user interface representing squares and
pieces with characters.
User input can be simply "enter" to have computer to play his best move,
or make a move using coordinate notation (eg a2a3). There is a check and
just legal moves will be carried out.
For under-promotion put in the 5th character of input move: 3 for Knight,
5 for Bishop, 6 for Rook, and 7 (default when no 5th char) for Queen.
The program originated from the smallest H.G. Muller's Micro-Max (1158
characters version http://home.hccnet.nl/h.g.muller/max1.html) squeezed
and enhanced (also for playing with full FIDE rules) by Stefano Maragò.
More information and updates at: http://smmax.sourceforge.net
(freely subscribe to the low traffic RSS feed for automatic updates).
For other communications/feedback: steve at ultra dot name.
*/
#define F;if(
#define W while(
#include <stdio.h>
C = 799, K = 8, X, Y; char c[9], b[128] = "VSUWTUSV"; D(k, x, n) { int i = 0, j, t, p, u, r, y, m = -C, v; do { F(u = b[i])&k) { j = ".H?LFICF"[p = r = u & 7] - 64; W r = p > 2 & r < 0 ? -r : 64 - "01/@AP@ABPOQ@NR_a@"[++j]) { y = i; do { t = b[y += r]F(p == 7 | !x) && j == 8 || !(r & 7) - !t&p < 3 | t & k || y & 136)break; v = t & k ? 1 : " !!#~#%)"[t & 7] - 32 F n&&v < 64){b[i] = 0, b[y] = u F p < 3 && y + r + 1 & 128)b[y] = (*c&c[4] ? c[4] : 55) - 48 | k, v += 9; v -= D(24 - k, 2, n - 1)F x & 1 & v > -64 & i == X & y == Y){F j == 8)b[y + (r >> 2 ^ 1)] = 0, b[y - r / 2] = 6 | k; return 0; }b[i] = u, b[y] = t; }F v > m){m = v F n > 4)X = i, Y = y; }t += p < 5 F x & 1 && (y & 112) + 6 * k == 128 & p < 3)t--; }W!t); } } }W i = i + 9 & 119); return m; }main() { X = 8; W X--)b[X + 112] = (b[X] -= 64) - 8, b[X + 16] = 18, b[X + 96] = 9; W 1){X = 128; W X--)putchar(X & 8 && (X -= 7) ? 10 : ".?+nkbrq?*?NKBRQ"[b[X] & 15]); gets(c); X = *c - 16 * c[1] + C, Y = c[2] - 16 * c[3] + C F!*c)D(K, 0, 5)F!D(K, 1, 1))K ^= 24; } }
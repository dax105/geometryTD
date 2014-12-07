varying vec2 vTexCoord;
 
// remember that you should draw a screen aligned quad
void main(void)
{
   gl_Position = ftransform();
 
   vTexCoord = gl_MultiTexCoord0.st;
}
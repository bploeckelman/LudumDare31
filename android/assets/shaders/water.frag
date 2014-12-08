#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float time;



void main()
{

  vec2 offset = vec2(sin(time * 2.5 * v_texCoords.x), cos(time * 1.5 * v_texCoords.y))* .01;
  vec4 color = texture2D(u_texture, offset + v_texCoords);

  gl_FragColor = color;
}

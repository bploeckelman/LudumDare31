#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform sampler2D u_texture2;
uniform sampler2D u_texture3;



void main()
{
  vec4 color = texture2D(u_texture, v_texCoords);
  vec4 color2 = texture2D(u_texture2, v_texCoords);
  vec4 blend = texture2D(u_texture3, v_texCoords);
  gl_FragColor = mix(color, color2, blend.g);
}

uniform sampler2D DiffuseMap;

void main() {
	vec4 color = texture2D(DiffuseMap, gl_TexCoord[0].st);
	
	gl_FragColor = vec4(1.0 - color.xyz.x, 1.0 - color.xyz.y, 1.0 - color.xyz.z, 1.0);

}
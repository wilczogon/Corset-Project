uniform sampler2D DiffuseMap;

void main() {
	vec4 color = texture2D(DiffuseMap, gl_TexCoord[0].st);
	float sum = (color.xyz.x + color.xyz.y + color.xyz.z)/3.0;
	
	gl_FragColor = vec4(sum);

}
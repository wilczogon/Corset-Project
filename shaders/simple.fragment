varying vec3 normal;
varying vec4 pos;
uniform sampler2D DiffuseMap;

void main() {
	vec4 color = texture2D(DiffuseMap, gl_TexCoord[0].st);
	vec4 matspec = vec4(0.2);
	float shininess = 0.5;
	vec4 lightspec = gl_LightSource[0].specular;
	vec4 lpos = gl_LightSource[0].position;
	vec4 s = -normalize(pos-lpos);

	vec3 light = s.xyz;
	vec3 n = normalize(normal);
	vec3 r = -reflect(light, n);
	r = normalize(r);
	vec3 v = -pos.xyz;
	v = normalize(v);
	
	vec4 diffuse  = color * max(0.0, dot(n, s.xyz)) * gl_LightSource[0].diffuse;
	vec4 specular = lightspec * matspec;// * pow(max(0.0, dot(r, v)), shininess);
	
	gl_FragColor = min(vec4(1.0), (diffuse + specular));
//	gl_FragColor = noise4(pos) != 0.0 ? vec4(1.0, 0.0, 0.0, 1.0) : vec4(0.0, 0.0, 1.0, 1.0);

}
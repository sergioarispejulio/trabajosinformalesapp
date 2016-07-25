class Generadortoken

	def initialize(modelo,sistema_operativo,ci_usuario)
		@modelo = modelo
		@sistema_operativo = sistema_operativo
		@ci_usuario = ci_usuario
	end

	def generar_token
		resultado = generar_string_de_ascii
		resultado = convertir_de_ascii_a_palabra(resultado)
		return resultado
	end

	def generar_string_de_ascii
		resultado = ""
		asciimodelo = convertir_a_ascii(@modelo)
		asciisistema_operativo = convertir_a_ascii(@sistema_operativo)
		asciici_usuario = @ci_usuario.to_s

		resultado = asciimodelo[0..((asciimodelo.length/2)-1)] + asciisistema_operativo[0..((asciisistema_operativo.length/2)-1)] + asciici_usuario[0..((asciici_usuario.length/2)-1)]
		resultado = resultado + asciimodelo[(asciimodelo.length/2)..(asciimodelo.length-1)] + asciisistema_operativo[(asciisistema_operativo.length/2)..(asciisistema_operativo.length-1)] + asciici_usuario[(asciici_usuario.length/2)..(asciici_usuario.length-1)]
		if(resultado.length >= 100)
			resultado = resultado[0..99]
		else
			while resultado.length < 100  do
			   resultado = resultado + resultado
			end
			resultado = resultado[0..99]
		end
		return resultado
	end

	def convertir_a_ascii(palabra)
		resultado = ""
		for i in 0..(palabra.length-1)
			resultado = resultado + palabra[i].ord.to_s
		end
		return resultado
	end

	def convertir_de_ascii_a_palabra(palabra)
		resultado = ""
		i = 0
		aux = ""
		while i < 100  do
			aux = ""
		    aux = palabra[i] + palabra[i+1]
		    aux = aux.to_i
		    if(aux <= 31)
		   		aux = aux + 32
		    end
		    resultado = resultado + aux.chr
		    i = i + 2
		end
		return resultado
	end

end
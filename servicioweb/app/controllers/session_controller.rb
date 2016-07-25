class SessionController < ApplicationController

	def crear
		token = ""
		status = ""
		id_usuario = ""
		@conexion = nil
		user = login(params[:nickname], params[:password], false)
		if user
			id_usuario = current_user.id
			generador = Generadortoken.new(params[:modelo].to_s,params[:so].to_s,current_user.ci)
			token = generador.generar_token
			@conexion = Conexionescelular.new()
			@conexion.token = token
			@conexion.token_mensajeria = params[:token_mensajeria]
			@conexion.modelo_celular = params[:modelo]
			@conexion.sistema_operativo = params[:so]
			@conexion.fecha_login = Date.new()
			@conexion.id_usuario = current_user.id
			if @conexion.save
				status = "OK"
			else
				token = ""
				id_usuario = ""
				status = "Error al iniciar seccion, vuelva a intentarlo"
			end
			logout
		else
			status = "Nombre de usuario o contraseña incorrectos"
		end
		@resultado = Array.new()
		@resultado << {:status => status, :token => token, :id_usuario => id_usuario}
		respond_to do |format|
	      format.json { render json: @resultado }
	      format.xml { render xml: @resultado }
	    end
	end

	def cerrar
		status = ""
		if(Conexionescelular.where(:token => params[:token], :id_usuario => params[:id_usuario], :fecha_logout => nil).exists?)

			generador = Generadortoken.new(params[:modelo].to_s,params[:so].to_s, User.find(params[:id_usuario]).ci.to_i)
			token = generador.generar_token
			
			if(token == params[:token])
				status = "OK"
				aux = Conexionescelular.where(:token => params[:token], :id_usuario => params[:id_usuario], :fecha_logout => nil)[0]
				aux.fecha_logout = Date.new()
				aux.save
			else
				status = "La información del token no coincide con las secciones que tiene activa"
			end

		else
			status = "No se encontro la seccion activa"
		end
		@resultado = Array.new()
		@resultado << {:status => status}
		respond_to do |format|
	      format.json { render json: @resultado }
	      format.xml { render xml: @resultado }
	    end
	end

	def existe_token
		status = ""
		if(Conexionescelular.where(:token => params[:token], :id_usuario => params[:id], :fecha_logout => nil).exists?)
			@conexion = Conexionescelular.where(:token => params[:token], :id_usuario => params[:id], :fecha_logout => nil)[0]
			@conexion.token_mensajeria = params[:token_mensajeria]
			@conexion.save
			status = "OK"
		else
			status = "No se encontro la seccion activa"
		end
		@resultado = Array.new()
		@resultado << {:status => status}
		respond_to do |format|
	      format.json { render json: @resultado }
	      format.xml { render xml: @resultado }
	    end
	end

end

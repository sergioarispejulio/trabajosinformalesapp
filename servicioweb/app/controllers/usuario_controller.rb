class UsuarioController < ApplicationController

	def crear
		status = ""
		if(User.where(:nickname => params[:nickname]).exists? == false)
			if(User.where(:telefono => params[:telefono]).exists? == false)
				@user = User.new
				@user.password = params[:password]
				@user.nickname = params[:nickname]
				@user.telefono = params[:telefono]
				@user.password_confirmation = params[:password_confirmation]
			    @user.email = params[:email]
			    @user.nombre = params[:nombre]
			    @user.apellido = params[:apellido]
			    @user.ciudad = params[:ciudad]
			    @user.ci = params[:ci]
			    @user.fecha_nacimiento = convertir_fecha(params[:fecha_nacimiento])
			    @user.url_foto = ""
			    if @user.save
			    	status = "OK"
			    	crear_todos_los_perfiles(@user.id)
			    else
			    	status = "Ocurrio un problema al guardar la información, vuelva a intentarlo"
			    end
			else
				status = "El telefono que ingreso ya esta en uso"
			end
		else
			status = "El nombre de usuario que ingreso ya esta en uso"
		end
	    @resultado = Array.new()
		@resultado << {:status => status}
		respond_to do |format|
	      format.json { render json: @resultado }
	      format.xml { render xml: @resultado }
	    end
	end

	def editar_informacion
		@condi = true
		if(User.where(:telefono => params[:telefono]).exists? == true)
			aux = User.where(:telefono => params[:telefono])[0]
			if(aux.id != params[:id])
				@condi = false
			end
		end
		status = ""
		if(@condi == true)
			@user = User.find(params[:id])
			@user.ciudad = params[:ciudad]
			@user.url_foto = params[:url_foto]
			if @user.save
				status = "OK"
			else
				status = "Ocurrio un problema al guardar la información, vuelva a intentarlo"
			end
		else
			status = "El telefono que ingreso ya esta en uso"
		end
		@resultado = Array.new()
		@resultado << {:status => status}
		respond_to do |format|
	      format.json { render json: @resultado }
	      format.xml { render xml: @resultado }
	    end
	end

	def obtener_lista_de_trabajadores_con_perfil_determinado
		@resultado = Array.new()
		@perfiles = Perfiltrabajador.where(:id_tipo_trabajo => params[:tipo_trabajo], :habilitado => true)
		@perfiles.each do |elemento|
			@resultado << User.find(elemento.id_usuario)
		end
		respond_to do |format|
	      format.json { render json: @resultado }
	      format.xml { render xml: @resultado }
	    end
	end

	def obtener_informacion_trabajador_con_perfil_determinado
		@status_perfil = false
		@status_comentarios = false
		@status_hizo_puntuacion = false
		@nombre_personas_comentario = Array.new()
		@comentarios = nil
		@perfil = nil
		@usuario = User.find(params[:id_trabajador])
		if(Perfiltrabajador.where(:id_usuario => params[:id_trabajador], :id_tipo_trabajo => params[:id_tipo_trabajo]).exists? == true)
			@perfil = Perfiltrabajador.where(:id_usuario => params[:id_trabajador], :id_tipo_trabajo => params[:id_tipo_trabajo])[0]
			@status_perfil = true
			if(Comentarioatrabajador.where(:id_perfil_trabajo => @perfil.id).exists? == true)
				@comentarios = Comentarioatrabajador.where(:id_perfil_trabajo => @perfil.id)
				@comentarios.each do |elemento|
					@nombre_personas_comentario << User.find(elemento.id_usuario_que_comenta).devolver_nombre_completo	
				end
				@status_comentarios = true
			end
			if(Puntuacion.where(:id_usuario_que_puntua => params[:id_usuario]).exists? == true)
				@status_hizo_puntuacion = true
			end
		end
		@resultado = Array.new()
		@resultado << {:usuario => @usuario, :status_perfil => @status_perfil, :status_comentarios => @status_comentarios, :perfil => @perfil, :comentarios => @comentarios, :nombres_comentarios => @nombre_personas_comentario, :status_hizo_puntuacion => @status_hizo_puntuacion }
		respond_to do |format|
	      format.json { render json: @resultado }
	      format.xml { render xml: @resultado }
	    end
	end

	def obtener_informacion_cliente
		@status_comentarios = false
		@comentarios = nil
		@nombre_personas_comentario = Array.new()
		@usuario = User.find(params[:id_cliente])
		if(Comentarioacliente.where(:id_usuario_que_recibe_comentario => params[:id_cliente]).exists? == true)
			@comentarios = Comentarioacliente.where(:id_usuario_que_recibe_comentario => params[:id_cliente])
			@comentarios.each do |elemento|
				@nombre_personas_comentario << User.find(elemento.id_trabajador_que_comenta).devolver_nombre_completo	
			end
			@status_comentarios = true
		end
		@resultado = Array.new()
		@resultado << {:usuario => @usuario, :status_comentarios => @status_comentarios, :comentarios => @comentarios, :nombres_comentarios => @nombre_personas_comentario}
		respond_to do |format|
	      format.json { render json: @resultado }
	      format.xml { render xml: @resultado }
	    end
	end

	def obtener_usuario_determinado
		@usuario = User.find(params[:id])
		@resultado = Array.new()
		@resultado << {:usuario => @usuario}
		respond_to do |format|
	      format.json { render json: @resultado }
	      format.xml { render xml: @resultado }
	    end
	end

	private

	def convertir_fecha(fecha)
		lista = fecha.split("/")
		result = Date.new(lista[0].to_i,lista[1].to_i,lista[2].to_i)
		return result
	end

	def crear_todos_los_perfiles(id)
		@lista = Tipotrabajo.all
		@lista.each do |elemento|
			aux = Perfiltrabajador.new
			aux.puntaje_promedio = 0
            aux.id_usuario = id
            aux.id_tipo_trabajo = elemento.id
            aux.habilitado = true
            aux.save
		end

	end

end

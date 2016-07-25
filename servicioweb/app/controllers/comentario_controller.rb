class ComentarioController < ApplicationController

	def dar_comentario_trabajador
		@resultado = Array.new()
		@comentario = Comentarioatrabajador.new
		@comentario.comentario = params[:comentario]
    	@comentario.id_usuario_que_comenta = params[:id_usuario_que_comenta]
    	@comentario.id_trabajador_que_recibe_comentario = params[:id_trabajador_que_recibe_comentario]
    	@comentario.id_perfil_trabajo = params[:id_perfil_trabajo]
    	@comentario.nombre_usuario_que_comenta = User.find(params[:id_usuario_que_comenta]).devolver_nombre_completo
    	if @comentario.save
    		status = "OK"
    	else
    		status = "Hay problemas con el servidor, vuelva a intentarlo mas tarde"
    	end
		@resultado << {:status => status}
		respond_to do |format|
	      format.json { render json: @resultado }
	      format.xml { render xml: @resultado }
	    end
	end

	def dar_comentario_cliente
		@resultado = Array.new()
		@comentario = Comentarioacliente.new
		@comentario.comentario = params[:comentario]
    	@comentario.id_trabajador_que_comenta = params[:id_trabajador_que_comenta]
    	@comentario.nombre_usuario_que_comenta = User.find(params[:id_trabajador_que_comenta])
    	@comentario.id_usuario_que_recibe_comentario = params[:id_usuario_que_recibe_comentario]
    	@comentario.nombre_usuario_que_comenta = User.find(params[:id_trabajador_que_comenta]).devolver_nombre_completo
    	if @comentario.save
    		status = "OK"
    	else
    		status = "Hay problemas con el servidor, vuelva a intentarlo mas tarde"
    	end
		@resultado << {:status => status}
		respond_to do |format|
	      format.json { render json: @resultado }
	      format.xml { render xml: @resultado }
	    end
	end

	def obtener_comentarios_trabajador
		@lista = Comentarioatrabajador.where(:id_trabajador_que_recibe_comentario => params[:id_trabajador_que_recibe_comentario], :id_perfil_trabajo => params[:id_perfil_trabajo])
		respond_to do |format|
	      format.json { render json: @lista  }
	      format.xml { render xml: @lista  }
	    end
	end

	def obtener_comentarios_cliente
		@lista = Comentarioacliente.where(:id_usuario_que_recibe_comentario => params[:id_usuario_que_recibe_comentario])
		respond_to do |format|
	      format.json { render json: @lista  }
	      format.xml { render xml: @lista  }
	    end
	end


end

class PeticionController < ApplicationController

	def acepter_o_rechazar_peticion_de_solicitud_de_trabajo
		status = ""
		@solicitud = Solicitudtrabajo.find(params[:id_solicitud])
		@solicitud.aceptado = params[:resultado]
		@resultado = Array.new()
		if @solicitud.save
			status = "OK"
			if(params[:resultado] == true)
				@trabajo = Trabajo.find(params[:id_trabajo])
				@trabajo.activo = false
				@trabajo.save
				notificator = Notificador.new
				notificator.enviar_mensaje_de_que_lo_seleccionaron(params[:id_solicitud])
			end
		else
			status =  "Error al enviar respuesta de la petición a su solicitud de trabajo, vuelva a intentarlo"
		end
		@resultado << {:status => status}
		respond_to do |format|
	      format.json { render json: @resultado }
	      format.xml { render xml: @resultado }
	    end
	end

	def mandar_peticion_de_solicitud_de_trabajo
		@resultado = Array.new()
		if (Solicitudtrabajo.where(:id_trabajo => params[:id_trabajo], :id_usuario  => params[:id_usuario]).exists? == false)
			@solicitud = Solicitudtrabajo.new
			@solicitud.fecha_solicitud = Date.new
			@solicitud.aceptado = false
			@solicitud.id_trabajo = params[:id_trabajo]
			@solicitud.id_usuario = params[:id_usuario]
			@solicitud.precio_medio = params[:precio_medio]
			if @solicitud.save
				notificator = Notificador.new
				notificator.enviar_mensaje_de_se_postularon_a_tu_oferta(params[:id_trabajo])
				@resultado << {:status => "OK"}
			else
				@resultado << {:status => "Error al enviar petición a la solicitud de trabajo, vuelva a intentarlo"}
			end
		else
			@resultado << {:status => "OK"}
		end
		respond_to do |format|
	      format.json { render json: @resultado }
	      format.xml { render xml: @resultado }
	    end
	end

	def obtener_peticiones_de_la_solicitud
		@lista = Array.new 
		solicitudes = Solicitudtrabajo.where(:id_trabajo => params[:id_trabajo])
		solicitudes.each do |solicitud|
			nombre_usuario = ""
			if(User.exists?(solicitud.id_usuario))
				nombre_usuario = User.find(solicitud.id_usuario).devolver_nombre_completo
			end
			@lista << { :solicitud => solicitud, :nombre_usuario => nombre_usuario }
		end
		respond_to do |format|
	      format.json { render json: @lista  }
	      format.xml { render xml: @lista  }
	    end
	end

	def obtener_mis_peticiones
		@lista = Array.new
		@solicitudes = Solicitudtrabajo.where(:id_usuario => params[:id_usuario])
		@solicitudes.each do |solicitud|
			trabajo = Trabajo.find(solicitud.id_trabajo)
			nombre = trabajo.titulo
			if(trabajo.activo == false)
				if(solicitud.aceptado == true)
					nombre = nombre + " - Te aceptaron en este trabajo"
				else
					nombre = nombre + " - No te aceptaron en este trabajo"
				end
			else
				nombre = nombre + " - Aún no seleccionaron a un trabajador"
			end
			@lista << { :solicitud => solicitud, :nombre_trabajo => nombre }
		end
		respond_to do |format|
	      format.json { render json: @lista  }
	      format.xml { render xml: @lista  }
	    end
	end

end

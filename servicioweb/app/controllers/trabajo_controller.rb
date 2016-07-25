class TrabajoController < ApplicationController

#rake db:migrate RAILS_ENV=test

	def crear
		@trabajo = Trabajo.new
		@trabajo.titulo = params[:titulo]
		@trabajo.descripcion = params[:descripcion]
		@trabajo.fecha_de_requerimiento = convertir_fecha(params[:fecha_de_requerimiento])
		@trabajo.id_tipo_trabajo = params[:id_tipo_trabajo]
		@trabajo.id_solicitante = params[:id_solicitante]
		@trabajo.url_foto = params[:url_foto]
		@trabajo.coordenadax = params[:coordenadax]
		@trabajo.coordenaday = params[:coordenaday]
		@trabajo.ciudad = params[:ciudad]
		@trabajo.nombre_solicitante = User.find(params[:id_solicitante]).devolver_nombre_completo
		@trabajo.activo = true
		@resultado = Array.new()
		if @trabajo.save
			@resultado << {:status => "OK"}
		else
			@resultado << {:status => "Error al crear solicitud de trabajo, vuelva a intentarlo"}
		end
		respond_to do |format|
	      format.json { render json: @resultado }
	      format.xml { render xml: @resultado }
	    end
	end


	def buscar_solicitudes_de_trabajo 
		@lista = Trabajo.where(:id_tipo_trabajo => params[:id_tipo_trabajo], :activo => params[:activo], :ciudad => params[:ciudad])
		respond_to do |format|
	      format.json { render json: @lista  }
	      format.xml { render xml: @lista  }
	    end
	end


	def lista_publicaciones_realizadas
		@trabajos = Trabajo.where(:id_solicitante => params[:id_solicitante])
		respond_to do |format|
	      format.json { render json: @trabajos  }
	      format.xml { render xml: @trabajos  }
	    end
	end

	
 	def buscar_por_id
 		@trabajo = Trabajo.find(params[:id_trabajo])
 		@resultado = Array.new
 		@resultado << {:trabajo => @trabajo}
		respond_to do |format|
	      format.json { render json: @resultado  }
	      format.xml { render xml: @resultado  }
	    end
 	end

 	def obtener_perfiles_de_trabajador
 		@trabajos = Perfiltrabajador.where(:id_usuario => params[:id_usuario])
		respond_to do |format|
	      format.json { render json: @trabajos  }
	      format.xml { render xml: @trabajos  }
	 	end
 	end


 	def actualizar_perfil_trabajo
 		@resultado = Array.new
 		aux = Perfiltrabajador.find(params[:id])
		aux.habilitado = params[:habilitado]
		if aux.save
			@resultado << {:status => "OK"}
		else
			@resultado << {:status => "Error al crear solicitud de trabajo, vuelva a intentarlo"}
		end
		respond_to do |format|
	      format.json { render json: @resultado  }
	      format.xml { render xml: @resultado  }
	    end
 	end


 	def devolver_tipos_de_trabajo
 		@resultado = Tipotrabajo.all
		respond_to do |format|
	      format.json { render json: @resultado  }
	      format.xml { render xml: @resultado  }
	    end
 	end

	private

	def convertir_fecha(fecha)
		lista = fecha.split("/")
		result = Date.new(lista[0].to_i,lista[1].to_i,lista[2].to_i)
		return result
	end

	def actualizarperfil(id_tipo_trabajo,id_usuario,condicion)
		aux = Perfiltrabajador.where(:id_usuario => id_usuario, :id_tipo_trabajo => id_tipo_trabajo)[0]
		aux.habilitado = condicion
		aux.save
	end
	
end

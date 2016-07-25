class PuntageController < ApplicationController

	def dar_puntaje_a_trabajador
		@resultado = Array.new()
		@puntaje = Puntuacion.new
		@puntaje.puntaje = params[:puntaje]
		@puntaje.id_usuario_que_puntua = params[:id_usuario_que_puntua]
		@puntaje.id_usuario_que_recibe_puntuacion = params[:id_usuario_que_recibe_puntuacion]
		@puntaje.id_perfil_trabajador = params[:id_perfil_trabajador]
		if @puntaje.save
			@perfil = Perfiltrabajador.find(params[:id_perfil_trabajador])
			@perfil.puntaje_promedio = promediar(@perfil.id)
			@perfil.save
			@resultado << {:status => "OK", :puntaje_promedio => @perfil.puntaje_promedio }
		else
			@resultado << {:status => "Error en el servidor web, vuelva a intentarlo", :puntaje_promedio => 0 }
		end
		@resultado << {:status => status}
		respond_to do |format|
	      format.json { render json: @resultado }
	      format.xml { render xml: @resultado }
	    end
		
	end


	private

	def promediar(id)
		tot = 0
		puntaje_promedio = 0
		lista = Puntuacion.where(:id_perfil_trabajador => id)
		if(lista.length != 0)
			lista.each do |elemento|
				tot = tot + elemento.puntaje
			end
			puntaje_promedio = (tot/lista.length)
		end
		return puntaje_promedio
	end


end

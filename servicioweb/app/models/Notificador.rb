require 'gcm'


class Notificador

	def enviar_mensaje_de_se_postularon_a_tu_oferta(id_trabajo)

		trabajo = Trabajo.find(id_trabajo)
		conexion = Conexionescelular.where(:id_usuario => trabajo.id_solicitante, :fecha_logout => nil)
		conexion.each do |elemento|
			gcm = GCM.new("AIzaSyAjhSfDPcwBA0FrTJMPMoTIAxOnd9yKyXU")
			registration_ids= [elemento.token_mensajeria] # an array of one or more client registration tokens
			options = {data: { title: "Nuevo postulante", message: "Un trabajor se postulo a su oferta: " + trabajo.titulo }}
			response = gcm.send(registration_ids, options)

		end
	end

	def enviar_mensaje_de_que_lo_seleccionaron(id_solicitud)

		solicitud = Solicitudtrabajo.find(id_solicitud)
		trabajo = Trabajo.find(solicitud.id_trabajo)
		conexion = Conexionescelular.where(:id_usuario => solicitud.id_usuario, :fecha_logout => nil)
		conexion.each do |elemento|
			gcm = GCM.new("AIzaSyAjhSfDPcwBA0FrTJMPMoTIAxOnd9yKyXU")
			registration_ids= [elemento.token_mensajeria] # an array of one or more client registration tokens
			options = {data: { title: "Lo seleccionaron", message: "A sido seleccionado en uno de los trabajos que se postulo: "  + trabajo.titulo }}
			response = gcm.send(registration_ids, options)

		end
		
	end

end
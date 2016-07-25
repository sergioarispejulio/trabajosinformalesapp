Rails.application.routes.draw do

  post "/crearusuario" => 'usuario#crear'
  post "/editarusuario" => "usuario#editar_informacion"

  post "/iniciarseccion" => 'session#crear'
  post "/verificartoken" => 'session#existe_token'
  post "/cerrarseccion" => 'session#cerrar'

  post "/creartrabajo" => "trabajo#crear"
  post "/buscartrabajoporid" => "trabajo#buscar_por_id"
  post "/obtenertrabajospublicados" => "trabajo#lista_publicaciones_realizadas"
  post "/obtenertrabajosactivos" => "trabajo#buscar_solicitudes_de_trabajo"
  post "/actualizarestadoperfiltrabajador" => "trabajo#actualizar_perfil_trabajo"
  post "/obtenermisperfilestrabajador" => "trabajo#obtener_perfiles_de_trabajador"
  
  post "/mandarpeticiondesolicituddetrabajo" => "peticion#mandar_peticion_de_solicitud_de_trabajo"
  post "/aceptarorechazarpeticiondesolicituddetrabajo" => "peticion#acepter_o_rechazar_peticion_de_solicitud_de_trabajo"
  post "/obtenerpeticionesalassolicitudes" => "peticion#obtener_peticiones_de_la_solicitud"
  post "/obtenermispeticionesasolicitudes" => "peticion#obtener_mis_peticiones"

  post "/darpuntajeatrabajador" => "puntage#dar_puntaje_a_trabajador"

  post "/obtenertiposdetrabajo" => "trabajo#devolver_tipos_de_trabajo" #faltan test para esto

  post "/comentartrabajador" => "comentario#dar_comentario_trabajador"
  post "/comentarcliente" => "comentario#dar_comentario_cliente"
  post "/obtenercomentarioscliente" => "comentario#obtener_comentarios_cliente"
  post "/obtenercomentariostrabajador" => "comentario#obtener_comentarios_trabajador"

  post "/obtenerlistatrabajadoresportipotrabajo" => "usuario#obtener_lista_de_trabajadores_con_perfil_determinado"
  post "/obtenerinfotrabajadordeterminado" => "usuario#obtener_informacion_trabajador_con_perfil_determinado"
  post "/obtenerinfocliente" => "usuario#obtener_informacion_cliente"
  post "/obtenerinfousuariodeterminado" => "usuario#obtener_usuario_determinado" #faltan test para esto



  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # You can have the root of your site routed with "root"
  # root 'welcome#index'

  # Example of regular route:
  #   get 'products/:id' => 'catalog#view'

  # Example of named route that can be invoked with purchase_url(id: product.id)
  #   get 'products/:id/purchase' => 'catalog#purchase', as: :purchase

  # Example resource route (maps HTTP verbs to controller actions automatically):
  #   resources :products

  # Example resource route with options:
  #   resources :products do
  #     member do
  #       get 'short'
  #       post 'toggle'
  #     end
  #
  #     collection do
  #       get 'sold'
  #     end
  #   end

  # Example resource route with sub-resources:
  #   resources :products do
  #     resources :comments, :sales
  #     resource :seller
  #   end

  # Example resource route with more complex sub-resources:
  #   resources :products do
  #     resources :comments
  #     resources :sales do
  #       get 'recent', on: :collection
  #     end
  #   end

  # Example resource route with concerns:
  #   concern :toggleable do
  #     post 'toggle'
  #   end
  #   resources :posts, concerns: :toggleable
  #   resources :photos, concerns: :toggleable

  # Example resource route within a namespace:
  #   namespace :admin do
  #     # Directs /admin/products/* to Admin::ProductsController
  #     # (app/controllers/admin/products_controller.rb)
  #     resources :products
  #   end
end

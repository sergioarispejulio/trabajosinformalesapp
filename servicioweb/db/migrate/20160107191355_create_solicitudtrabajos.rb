class CreateSolicitudtrabajos < ActiveRecord::Migration
  def change
    create_table :solicitudtrabajos do |t|
      t.date :fecha_solicitud
      t.boolean :aceptado
      t.integer :precio_medio
      t.integer :id_trabajo
      t.integer :id_usuario
      t.timestamps null: false
    end
  end
end

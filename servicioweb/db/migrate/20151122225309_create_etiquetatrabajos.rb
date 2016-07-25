class CreateEtiquetatrabajos < ActiveRecord::Migration
  def change
    create_table :etiquetatrabajos do |t|
      t.integer :id_etiqueta
      t.integer :id_trabajo
      t.timestamps null: false
    end
  end
end

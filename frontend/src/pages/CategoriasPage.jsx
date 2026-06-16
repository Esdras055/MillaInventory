import { Edit, Plus, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
import Swal from "sweetalert2";

import {
  getCategorias,
  createCategoria,
  updateCategoria,
  deleteCategoria,
} from "../api/categoriasApi";

export default function CategoriasPage() {
  const [categorias, setCategorias] = useState([]);
  const [nombre, setNombre] = useState("");
  const [editingCategoria, setEditingCategoria] = useState(null);

  async function loadData() {
    try {
      const response = await getCategorias();
      setCategorias(response.data);
    } catch (error) {
      console.error(error);
    }
  }

  useEffect(() => {
    loadData();
  }, []);

  async function handleSubmit(e) {
    e.preventDefault();

    try {
      if (editingCategoria) {
        await updateCategoria(editingCategoria.id, {
          nombre,
        });

        Swal.fire(
          "Actualizada",
          "La categoría fue actualizada correctamente",
          "success"
        );
      } else {
        await createCategoria({
          nombre,
        });

        Swal.fire(
          "Creada",
          "La categoría fue creada correctamente",
          "success"
        );
      }

      setNombre("");
      setEditingCategoria(null);
      loadData();

    } catch (error) {

      Swal.fire(
        "Error",
        error.response?.data?.message || "No se pudo guardar",
        "error"
      );
    }
  }

  async function handleDelete(categoria) {

    const result = await Swal.fire({
      icon: "warning",
      title: `Eliminar ${categoria.nombre}?`,
      showCancelButton: true,
      confirmButtonText: "Eliminar",
    });

    if (!result.isConfirmed) return;

    try {

      await deleteCategoria(categoria.id);

      Swal.fire(
        "Eliminada",
        "La categoría fue eliminada correctamente",
        "success"
      );

      loadData();

    } catch (error) {

      Swal.fire(
        "Error",
        error.response?.data?.message ||
        "No se puede eliminar la categoría porque tiene productos asociados",
        "error"
      );
    }
  }

  function handleEdit(categoria) {
    setEditingCategoria(categoria);
    setNombre(categoria.nombre);
  }

  return (
    <section>

      <h1 className="mb-6 text-2xl font-bold">
        Categorías
      </h1>

      <form
        onSubmit={handleSubmit}
        className="mb-8 flex gap-3"
      >

        <input
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
          placeholder="Nombre de categoría"
          className="rounded-lg border px-4 py-2"
        />

        <button
          className="flex items-center gap-2 rounded-lg bg-emerald-600 px-4 py-2 text-white"
        >
          <Plus size={18} />

          {editingCategoria
            ? "Actualizar"
            : "Crear"}
        </button>

      </form>

      <table className="w-full">

        <thead>

          <tr>

            <th>ID</th>
            <th>Nombre</th>
            <th>Acciones</th>

          </tr>

        </thead>

        <tbody>

          {categorias.map((categoria) => (

            <tr key={categoria.id}>

              <td>{categoria.id}</td>

              <td>{categoria.nombre}</td>

              <td>

                <div className="flex gap-2">

                  <button
                    className="rounded border p-2"
                    onClick={() => handleEdit(categoria)}
                  >
                    <Edit size={16} />
                  </button>

                  <button
                    className="rounded border border-red-200 p-2 text-red-600"
                    onClick={() => handleDelete(categoria)}
                  >
                    <Trash2 size={16} />
                  </button>

                </div>

              </td>

            </tr>

          ))}

        </tbody>

      </table>

    </section>
  );
}
import { Navigate, Route, Routes } from "react-router-dom";
import AppLayout from "./components/AppLayout";
import ProtectedRoute from "./routes/ProtectedRoute";
import RoleRoute from "./routes/RoleRoute";

import BodegasPage from "./pages/BodegasPage";
import DashboardPage from "./pages/DashboardPage";
import EntradasPage from "./pages/EntradasPage";
import LoginPage from "./pages/LoginPage";
import ReportesPage from "./pages/ReportesPage";
import SalidasPage from "./pages/SalidasPage";
import UsuariosPage from "./pages/UsuariosPage";
import ProductosPage from "./pages/ProductosPage";
import CategoriasPage from "./pages/CategoriasPage";
import MarcasPage from "./pages/MarcasPage";
import ProveedoresPage from "./pages/ProveedoresPage";

import "./App.css";

function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />

      <Route element={<ProtectedRoute />}>
        <Route element={<AppLayout />}>
          <Route path="/dashboard" element={<DashboardPage />} />

          <Route element={<RoleRoute allowedRoles={["ROLE_ADMIN"]} />}>
            <Route path="/usuarios" element={<UsuariosPage />} />

            <Route
              path="/entradas"
              element={<EntradasPage />}
            />

            <Route
              path="/salidas"
              element={<SalidasPage />}
            />
          </Route>

          <Route element={<RoleRoute allowedRoles={["ROLE_ADMIN", "ROLE_ANALYST"]} />}>
            <Route path="/reportes" element={<ReportesPage />} />
          </Route>

          <Route path="/productos" element={<ProductosPage />} />

          <Route
            path="/bodegas"
            element={<BodegasPage />}
          />

          <Route path="/categorias" element={<CategoriasPage />} />

          <Route path="/marcas" element={<MarcasPage />} />

          <Route path="/proveedores" element={<ProveedoresPage />} />
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}

export default App;
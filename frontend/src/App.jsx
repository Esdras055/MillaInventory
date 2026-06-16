import { Navigate, Route, Routes } from "react-router-dom";
import AppLayout from "./components/AppLayout";
import ProtectedRoute from "./routes/ProtectedRoute";
import RoleRoute from "./routes/RoleRoute";
import BodegasPage from "./pages/BodegasPage";
import DashboardPage from "./pages/DashboardPage";
import EntradasPage from "./pages/EntradasPage";
import LoginPage from "./pages/LoginPage";
import ModulePage from "./pages/ModulePage";
import ReportesPage from "./pages/ReportesPage";
import SalidasPage from "./pages/SalidasPage";
import UsuariosPage from "./pages/UsuariosPage";
import ProductosPage from "./pages/ProductosPage";
import CategoriasPage from "./pages/CategoriasPage";
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
         <Route path="/productos" element={<ProductosPage />}/>
          
          <Route
            path="/bodegas"
            element={<BodegasPage />}
          />
          <Route
            path="/categorias"
            element={
              <ModulePage
                title="Categorias"
                description="Gestion de categorias de productos."
              />
            }
          />
          <Route
            path="/marcas"
            element={<ModulePage title="Marcas" description="Gestion de marcas." />}
          />
          <Route
            path="/proveedores"
            element={
              <ModulePage
                title="Proveedores"
                description="Gestion de proveedores del inventario."
              />
            }
          />
        </Route>
      </Route>
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}

export default App;

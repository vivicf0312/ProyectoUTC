package prueba;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import utilidades.Enumeracion.Operaciones;
import utilidades.Logging;
import capaDatos.ArticuloDAO;
import capaDatos.DAOFactory;
import capaDatos.RegistroDAO;
import capaDatos.RolDAO;
import capaDatos.UbicacionDAO;
import capaDatos.UsuarioDAO;
import capaNegocio.Articulo;
import capaNegocio.Registro;
import capaNegocio.Rol;
import capaNegocio.Ubicacion;
import capaNegocio.Usuario;

public class PruebaPrincipal {
	Logger logger = Logging.obtenerServerLogger();
	DAOFactory df = DAOFactory.obtenerInstancia();
	Rol rol;
	Usuario usuario;
	Articulo articulo;
	Ubicacion ubicacion;
	Registro registro;

	public void pruebaAgregar() throws Exception {
		// ADD

		HashSet<String> operaciones = new HashSet<String>();
		operaciones.add("AGREGAR_USUARIO");
		operaciones.add("AGREGAR_ARTICULO");
		operaciones.add("AGREGAR_ROL");

		HashSet<Operaciones> operaciones2 = new HashSet<Operaciones>();
		operaciones2.add(Operaciones.EDITAR_ARTICULO);
		operaciones2.add(Operaciones.MOSTRAR_UBICACION);

		rol = new Rol("Profesor", operaciones2);
		RolDAO rolDAO = (RolDAO) df.obtenerDAO(rol.getClass().getSimpleName());
		int result = rolDAO.agregar(rol);
		logger.info("Rol agregado " + result);
		rol = rolDAO.mostrarTodo().get(0);
		logger.info("Rol = " + rol);

		usuario = new Usuario("Marta", "Castro", "Leon", "mc345", "java1",
				rol.getIdRol());
		UsuarioDAO usuarioDAO = (UsuarioDAO) df.obtenerDAO(usuario.getClass()
				.getSimpleName());
		int result2 = usuarioDAO.agregar(usuario);
		logger.info("Usuario agreagado " + result2);

		Usuario usuarioBuscar = usuarioDAO.buscar(usuario.getNombreUsuario(),
				usuario.getClave());
		logger.info("Usuario encontrado " + usuarioBuscar);

		usuario = usuarioDAO.mostrarTodo().get(0);
		logger.info("Usuario = " + usuario);

		ubicacion = new Ubicacion("clase 3");
		UbicacionDAO ubicacionDAO = (UbicacionDAO) df.obtenerDAO(ubicacion
				.getClass().getSimpleName());
		int result3 = ubicacionDAO.agregar(ubicacion);
		logger.info("Ubicacion agregada " + result3);
		ubicacion = ubicacionDAO.mostrarTodo().get(0);
		logger.info("Ubicacion = " + ubicacion);

		articulo = new Articulo(1, 1, 5, "lapiz", "na", "na", "na", "nuevos",
				ubicacion.getIdUbicacion(), "donacion", "");
		ArticuloDAO articuloDAO = (ArticuloDAO) df.obtenerDAO(articulo
				.getClass().getSimpleName());
		int result4 = articuloDAO.agregar(articulo);
		logger.info("Articulo agreagado " + result4);
		articulo = articuloDAO.mostrarTodo().get(0);
		logger.info("Articulo = " + articulo);

		registro = new Registro("Usuario agregado", usuario.getIdUsuario(),
				null);
		RegistroDAO registroDAO = (RegistroDAO) df.obtenerDAO(registro
				.getClass().getSimpleName());
		int result5 = registroDAO.agregar(registro);
		logger.info("Registro agregado " + result5);
		registro = registroDAO.mostrarTodo().get(0);
		logger.info("Registro = " + registro);
	}

	public void pruebaActualizar() throws Exception {
		// UPDATE
		HashSet<String> operaciones2 = new HashSet<String>();
		operaciones2.add("AGREGAR ARTICULO");
		operaciones2.add("CAMBIAR CLAVE");

		rol.setDescripcion("Director");
		RolDAO rolDAO = (RolDAO) df.obtenerDAO(rol.getClass().getSimpleName());
		boolean result = rolDAO.actualizar(rol);
		logger.info("Rol actualizado " + result);
		logger.info("Rol actualizado = " + rolDAO.mostrar(rol.getIdRol()));

		usuario.setClave("java123");
		usuario.setApellido1("Camacho");
		UsuarioDAO usuarioDAO = (UsuarioDAO) df.obtenerDAO(usuario.getClass()
				.getSimpleName());
		boolean result2 = usuarioDAO.actualizar(usuario);
		logger.info("Usuario actualizado" + result2);
		logger.info("Usuario actualizado = "
				+ usuarioDAO.mostrar(usuario.getIdUsuario()));

		ubicacion.setDescripcion("oficina");
		UbicacionDAO ubicacionDAO = (UbicacionDAO) df.obtenerDAO(ubicacion
				.getClass().getSimpleName());
		boolean result3 = ubicacionDAO.actualizar(ubicacion);
		logger.info("Ubicacion actualizada" + result3);
		logger.info("Ubicacion actualizada = "
				+ ubicacionDAO.mostrar(ubicacion.getIdUbicacion()));

		articulo.setCondicion("Muy buena");
		articulo.setIdEtiqueta("4512586");
		ArticuloDAO articuloDAO = (ArticuloDAO) df.obtenerDAO(articulo
				.getClass().getSimpleName());
		boolean result4 = articuloDAO.actualizar(articulo);
		logger.info("Articulo actualizado " + result4);
		logger.info("Articulo actualizado = "
				+ articuloDAO.mostrar(articulo.getIdArticulo()));

	}

	public void pruebaEliminar() throws Exception {
		// DELETE
		HashSet<String> operaciones2 = new HashSet<String>();
		operaciones2.add("AGREGAR ARTICULO");
		operaciones2.add("CAMBIAR CLAVE");

		UsuarioDAO usuarioDAO = (UsuarioDAO) df.obtenerDAO(usuario.getClass()
				.getSimpleName());
		boolean result1 = usuarioDAO.eliminar(usuario.getIdUsuario());
		logger.info("Usuario eliminado " + result1);

		RolDAO rolDAO = (RolDAO) df.obtenerDAO(rol.getClass().getSimpleName());
		boolean result2 = rolDAO.eliminar(rol.getIdRol());
		logger.info("Rol eliminado " + result2);

		ArticuloDAO articuloDAO = (ArticuloDAO) df.obtenerDAO(articulo
				.getClass().getSimpleName());
		boolean result3 = articuloDAO.eliminar(articulo.getIdArticulo());
		logger.info("Articulo eliminado " + result3);

		UbicacionDAO ubicacionDAO = (UbicacionDAO) df.obtenerDAO(ubicacion
				.getClass().getSimpleName());
		boolean result4 = ubicacionDAO.eliminar(ubicacion.getIdUbicacion());
		logger.info("Ubicacion eliminada " + result4);

		RegistroDAO registroDAO = (RegistroDAO) df.obtenerDAO(registro
				.getClass().getSimpleName());
		boolean result5 = registroDAO.eliminar(registro.getIdRegistro());
		logger.info("Registro Eliminado " + result5);
	}

	public void pruebaSeleccionarTodo() throws Exception {

		UsuarioDAO usuarioDAO = (UsuarioDAO) df.obtenerDAO(usuario.getClass()
				.getSimpleName());
		List<Usuario> result1 = usuarioDAO.mostrarTodo();
		assert (result1.isEmpty());
		logger.info("Tabla usuarios esta vacia" + result1);

		RolDAO rolDAO = (RolDAO) df.obtenerDAO(rol.getClass().getSimpleName());
		List<Rol> result2 = rolDAO.mostrarTodo();
		assert (result2.isEmpty());
		logger.info("Tabla roles esta vacia " + result2);

		ArticuloDAO articuloDAO = (ArticuloDAO) df.obtenerDAO(articulo
				.getClass().getSimpleName());
		List<Articulo> result3 = articuloDAO.mostrarTodo();
		assert (result3.isEmpty());
		logger.info("Tabla articulos esta vacia " + result3);

		UbicacionDAO ubicacionDAO = (UbicacionDAO) df.obtenerDAO(ubicacion
				.getClass().getSimpleName());
		List<Ubicacion> result4 = ubicacionDAO.mostrarTodo();
		assert (result4.isEmpty());
		logger.info("Tabla ubicaciones esta vacia " + result4);

		RegistroDAO registroDAO = (RegistroDAO) df.obtenerDAO(registro
				.getClass().getSimpleName());
		List<Registro> result5 = registroDAO.mostrarTodo();
		assert (result5.isEmpty());
		logger.info("Tabla registros esta vacia " + result5);
	}

	public static void main(String[] args) throws Exception {

		// Logging.initLogger();

		PruebaPrincipal test = new PruebaPrincipal();

		test.pruebaAgregar();

		test.pruebaActualizar();

		test.pruebaEliminar();

		test.pruebaSeleccionarTodo();
	}

}

Feature: Login de Medico

  Scenario: 1- Ingreso de sesion de Medico con credenciales correctas
    Given Existe el medico "Perez" registrado con mail "testuser@example.com" y contrasenia "testpassword"
    When Inicio sesion
    Then Devuelve mensaje de inicio de sesion exitoso

  Scenario: 2- Ingreso de sesion de Medico con credenciales erroneas
    Given Existe el medico "Perez" registrado con mail "testuser@example.com" y contrasenia "testpassword123"
    When Inicio sesion
    Then Devuelve mensaje de Error de Autenticacion

  Scenario: 3- Ingreso de sesion de Medico no registrado
    Given El medico "Ivanoff" con mail "jeremiasivanof@example.com" y contrasenia "contrasenia123"
    When Inicio sesion
    Then Devuelve mensaje de medico no registrado
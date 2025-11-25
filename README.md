# Mestre das Palavras

`Mestre das Palavras` é um jogo de adivinhação de palavras (inspirado em jogos como Termo/Wordle) desenvolvido como um projeto completo de aplicativo Android. O aplicativo demonstra a implementação da arquitetura MVVM, persistência de dados local com Room, integração com serviços em nuvem (Firebase) e uma clara separação de lógica entre perfis de usuário (Jogador e Administrador).

Este projeto foi desenvolvido para atender aos requisitos de um trabalho final de desenvolvimento móvel, focando na aplicação de conceitos modernos de Android, como Jetpack Compose, Flow, Coroutines e integração de bancos de dados.

## Funcionalidades Principais

O aplicativo é dividido em dois modos de acesso principais:

### 1. Modo Jogador

* **Jogo Principal:** O jogador deve adivinhar uma palavra secreta de 5 letras em 6 tentativas.
* **Feedback Visual:** A cada tentativa, as letras recebem cores para indicar:
    * **Verde:** Letra correta na posição correta.
    * **Amarelo:** Letra correta na posição errada.
    * **Cinza:** Letra incorreta.
* **Ranking Local:** Ao vencer, o nome do jogador e o número de tentativas são salvos em um banco de dados local (Room).
* **Tela de Ranking:** Os jogadores podem visualizar as 10 melhores pontuações (ordenadas pelo menor número de tentativas).

### 2. Modo Administrador

* **Acesso Restrito:** O administrador faz login através de uma tela dedicada, utilizando o **Firebase Authentication** para segurança.
* **Dashboard de Admin:** Após o login, o admin tem acesso a um painel para:
    * Gerenciar o banco de palavras.
    * Limpar o ranking de jogadores.
* **Gerenciamento de Palavras (CRUD):**
    * O admin pode Adicionar, Editar e Excluir as palavras que são usadas no jogo.
    * Este gerenciamento é sincronizado diretamente com o **Firebase Firestore**.
* **Sincronização de Dados:** As palavras gerenciadas pelo admin no Firestore são sincronizadas com o banco de dados Room local do app para que o jogo possa selecionar palavras aleatórias.

## Telas do Aplicativo

1.  **Tela Inicial:** Apresenta o nome do jogo e os botões de navegação: "Jogar", "Ranking" e "Admin".
2.  **Tela de Jogo:** Interface principal do jogo com a grade de 6x5, teclado e lógica de tentativa.
3.  **Tela de Ranking:** Exibe uma `LazyColumn` com a lista de pontuações salvas no Room.
4.  **Tela de Login (Admin):** Tela para login de administrador usando Firebase Auth.
5.  **Dashboard (Admin):** Painel de controle do administrador.
6.  **Gerenciar Palavras (Admin):** Tela de CRUD para a base de palavras no Firestore.

## Arquitetura e Tecnologias Utilizadas

Este projeto segue uma arquitetura **MVVM (Model-View-ViewModel)** limpa, separando a lógica de negócios da interface do usuário.

* **Linguagem:** [Kotlin](https://kotlinlang.org/)
* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
* **Arquitetura:** MVVM
* **Navegação:** [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
* **Banco de Dados Local:** [Room](https://developer.android.com/jetpack/androidx/releases/room)
    * Utilizado para armazenar duas entidades: `Ranking` (placar dos jogadores) e `Palavra` (cache local das palavras do jogo).
* **Banco de Dados Remoto (Nuvem):** [Firebase Firestore](https://firebase.google.com/products/firestore)
    * Utilizado como a fonte principal da verdade para as palavras do jogo, gerenciadas pelo Admin.
* **Autenticação:** [Firebase Authentication](https://firebase.google.com/products/auth)
    * Usado para proteger a rota de Administrador.
* **Programação Assíncrona:** [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) e [Flow](https://kotlinlang.org/docs/flow.html)
    * Usados para operações de banco de dados e atualizações reativas da UI.
* **Injeção de Dependência:** ViewModel Factory
    * Implementação manual de `ViewModelProvider.Factory` para injetar os Repositórios nos ViewModels (`JogoViewModel` e `AdminViewModel`).

## Como Executar o Projeto

Para clonar e executar este projeto, você precisará configurar o ambiente Firebase, pois o arquivo de configuração sensível não é versionado.

1.  **Clone o Repositório:**

    ```bash
    git clone [https://github.com/Kumegawwa/MestreDasPalavras-Trabalho-Android.git](https://github.com/Kumegawwa/MestreDasPalavras-Trabalho-Android.git)
    ```

2.  **Abra no Android Studio:**

    * Abra o projeto no Android Studio (versão compatível com Gradle 8.0+).

3.  **Configuração do Firebase (⚠️ Importante):**

    > **Nota de Segurança:** O arquivo `google-services.json` contém credenciais específicas do projeto e **não está incluído neste repositório** (listado no `.gitignore`). Sem este arquivo, o projeto apresentará erros de compilação.

    * Acesse o [Console do Firebase](https://console.firebase.google.com/).
    * Crie um novo projeto Android.
    * Use o `package_name`: `com.example.myapplication` (conforme definido no `build.gradle`).
    * Baixe o arquivo `google-services.json` gerado pelo Firebase.
    * **Mova o arquivo baixado para a pasta `app/` dentro da raiz do projeto.**

4.  **Habilitar Serviços do Firebase:**

    * No console do Firebase, habilite os seguintes serviços:
        * **Authentication:** Vá para a aba "Authentication", clique em "Sign-in method" e habilite a opção "Email/Senha". (Crie um usuário admin manual para teste, ex: `admin@admin.com` / `admin1234`).
        * **Firestore:** Vá para a aba "Firestore Database", crie um banco de dados e inicie no modo de produção (ou teste).

5.  **Criar Coleção no Firestore:**

    * Dentro do seu banco de dados Firestore, crie manualmente uma coleção chamada `palavras`.
    * Adicione documentos a esta coleção. Cada documento deve ter um único campo `String` chamado `palavra` (ex: `palavra: "TESTE"`).

6.  **Compile e Execute:**

    * O Android Studio irá sincronizar o Gradle. Após a sincronização e com o JSON na pasta correta, compile e execute o aplicativo em um emulador ou dispositivo físico.

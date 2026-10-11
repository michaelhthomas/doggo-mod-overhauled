{
  description = "Doggo Mod Overhauled";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  };

  outputs = {
    self,
    nixpkgs,
  }: let
    systems = [
      "x86_64-linux"
      "aarch64-linux"
      "x86_64-darwin"
      "aarch64-darwin"
    ];
    forAllSystems = nixpkgs.lib.genAttrs systems;
  in {
    devShells = forAllSystems (
      system: let
        pkgs = nixpkgs.legacyPackages.${system};

        jdk = pkgs.jdk17;

        # Runtime libraries LWJGL/Minecraft need to launch the client (`runClient`).
        runtimeLibs =
          if pkgs.stdenv.hostPlatform.isLinux
          then
            with pkgs; [
              libGL
              libGLU
              openal
              libpulseaudio
              alsa-lib
              udev
              libxkbcommon
              libX11
              libXcursor
              libXrandr
              libXi
              libXxf86vm
              libXext
              libXrender
              libXtst
            ]
          else [];
      in {
        default = pkgs.mkShell {
          packages =
            [
              jdk

              # Build / data tooling
              pkgs.git
              pkgs.jq
              pkgs.python3
              pkgs.unzip
              pkgs.zip
              pkgs.curl
              pkgs.cacert
            ]
            ++ runtimeLibs;

          JAVA_HOME = "${jdk}";
          LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath runtimeLibs;

          shellHook = ''
            echo "Doggo Mod Overhauled - dev shell"
            echo "  Java: $JAVA_HOME ($(java -version 2>&1 | head -n1))"
          '';
        };
      }
    );
  };
}

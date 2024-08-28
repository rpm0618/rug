{
    description = "A technical utility mod for Minecraft 1.8 (like a carpet, but worse)";

    inputs = {
        nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
        flake-utils.url = "github:numtide/flake-utils";
    };

    outputs = { self, nixpkgs, flake-utils, ... }:
        flake-utils.lib.eachDefaultSystem(system:
            let
                pkgs = import nixpkgs {
                    inherit system;
                    config = {
                        allowBroken = true;
                        allowUnfree = true;
                        permittedInsecurePackages = [
                            "python-2.7.18.8"
                        ];
                    };
                };
            in {
                devShells.default = pkgs.mkShell {
                    name = "rug";
                    packages = with pkgs; [
                        diffutils
                        gnupatch
                        # java-language-server
                        jdt-language-server
                        jdk
                        libzip
                        minecraft
                        python2
                        unzip
                        wget
                    ];
                };
            }
        );
}

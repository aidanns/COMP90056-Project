# Streams Project

## Author

Aidan Nagorcka-Smith (aidann@student.unimelb.edu.au)

## Running

1. Install `vagrant` for your system from http://downloads.vagrantup.com/
2. `vagrant plugin install vagrant-vbguest` to install the virutal box plugin for vagrant. This makes sure that virtualbox extensions are installed in the virtual machine.
3. Install `virtualbox` for your system from https://www.virtualbox.org/
4. `vagrant up; vagrant vbguest --do install; vagrant reload` from the root of the project to build and start a virtual machine running the application, then install the virtualbox additions.
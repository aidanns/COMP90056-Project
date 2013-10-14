# Configuration file to spin up machine to run the project on.
# Author: Aidan Nagorcka-Smith (aidanns@gmail.com)

VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  # Define which virtual machine to use as a base for this one.
  config.vm.box = "precise64"
  config.vm.box_url = "http://files.vagrantup.com/precise64.box"

  # Don't automatically install the virtualbox extensions.
  config.vbguest.auto_update = false

  # Create the shell script used to create the system in pkg_cmd.

  # Install Docker.
  pkg_cmd = "wget -q -O - https://get.docker.io/gpg | apt-key add -;" \
    "echo deb http://get.docker.io/ubuntu docker main > /etc/apt/sources.list.d/docker.list;" \
    "apt-get update -qq; apt-get install -q -y --force-yes lxc-docker; "

  # Install the Ubuntu raring backported kernel.
  pkg_cmd << "apt-get update -qq; apt-get install -q -y linux-image-generic-lts-raring; "

  # Activate the new kernel.
  pkg_cmd << "shutdown -r +1; "

  config.vm.provision :shell, :inline => pkg_cmd

  # Create a host-only network to the vagrant instance.
  config.vm.network :private_network, ip: "192.168.33.10"

end

# Configuration file to spin up machine to run the project on.
# Author: Aidan Nagorcka-Smith (aidanns@gmail.com)

VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  # Define which virtual machine to use as a base for this one.
  config.vm.box = "ubuntu-raring-server-amd64_13.04"
  config.vm.box_url = "http://cloud-images.ubuntu.com/vagrant/raring/current/raring-server-cloudimg-amd64-vagrant-disk1.box"

  # Create the shell script used to create the system in pkg_cmd.

  # Install extra drivers to get AUFS support.
  pkg_cmd = "apt-get update; apt-get install -y linux-image-extra-`uname -r`; "

  # Add docker repo.
  pkg_cmd << "wget -q -O - https://get.docker.io/gpg | apt-key add -; "
  pkg_cmd << "echo deb http://get.docker.io/ubuntu docker main > /etc/apt/sources.list.d/docker.list; "
  
  # Install docker.
  pkg_cmd << "apt-get update; apt-get install -y lxc-docker; "

  # Configure docker so it can be used by the vagrant user.
  pkg_cmd << "gpasswd -a vagrant docker; "
  pkg_cmd << "service docker restart; "
  pkg_cmd << "reboot; "

  config.vm.provision :shell, :inline => pkg_cmd

  # Create a host-only network to the vagrant instance.
  config.vm.network :private_network, ip: "192.168.33.10"

end

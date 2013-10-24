# Configuration file to spin up machine to run the project on.
# Author: Aidan Nagorcka-Smith (aidanns@gmail.com)

VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  # Define which virtual machine to use as a base for this one.
  config.vm.box = "ubuntu-precise-server-amd64_12.04"
  config.vm.box_url = "http://cloud-images.ubuntu.com/vagrant/precise/current/precise-server-cloudimg-amd64-vagrant-disk1.box"

  # Provision the box using a shell script.
  config.vm.provision "shell", path: "vagrant_provision_root.sh"
  config.vm.provision "shell", path: "vagrant_provision_root.sh"

  # Create a host-only network to the vagrant instance.
  config.vm.network :private_network, ip: "192.168.33.10"

  config.vm.provider "virtualbox" do |v|
    v.customize ["modifyvm", :id, "--memory", "2048"]
    v.customize ["modifyvm", :id, "--cpuexecutioncap", "50"]
    v.customize ["modifyvm", :id, "--cpus", "1"]
  end

end

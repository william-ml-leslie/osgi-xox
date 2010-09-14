gem 'buildr-bnd', :version => '0.0.5'
gem 'buildr-iidea', :version => '0.0.7'
gem 'buildr-ipojo', :version => '0.0.1'

require 'buildr_bnd'
require 'buildr_iidea'
require 'buildr_ipojo'

repositories.remote << 'https://repository.apache.org/content/repositories/releases'
repositories.remote << 'http://repository.ops4j.org/maven2' # Pax-*
repositories.remote << 'http://download.java.net/maven/2' # OpenMQ
repositories.remote << 'http://repository.buschmais.com/releases' # Maexo
repositories.remote << 'http://www.ibiblio.org/maven2'
repositories.remote << 'http://repository.springsource.com/maven/bundles/external'
repositories.remote << 'http://repository.code-house.org/content/repositories/release' # OSGi - jmx RI

repositories.remote << Buildr::Bnd.remote_repository
repositories.remote << Buildr::Ipojo.remote_repository

IPOJO_ANNOTATIONS = Buildr::Ipojo.annotation_artifact

OSGI_CORE = 'org.apache.felix:org.osgi.core:jar:1.4.0'
OSGI_COMPENDIUM = 'org.apache.felix:org.osgi.compendium:jar:1.4.0'


KARAF_DIR= "C:/dev/apache-karaf-2.0.1/-SNAPSHOT/deploy"

desc 'osgi-xox: OSGi tic-tac-toe testbed'
define('osgi-xox') do
  project.version = '0.1'
  project.group = 'osgi-xox'
  compile.options.source = '1.6'
  compile.options.target = '1.6'
  compile.options.lint = 'all'
  compile.with OSGI_CORE, OSGI_COMPENDIUM#, IPOJO_ANNOTATIONS
  #project.ipojoize!

  package(:bundle).tap do |bnd|
   bnd['Export-Package'] = "au.com.stocksoftware.wleslie.*;version=#{version}"
  end
end

desc "Deploy files require to run to a Karaf instance"
task :deploy_to_karaf do
  cp artifacts([project('osgi-xox').package(:bundle)]).collect { |a| a.invoke; a.to_s },
     "#{KARAF_DIR}/deploy/"
end

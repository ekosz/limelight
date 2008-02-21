require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/block'
require 'limelight/players/text_area'

describe Limelight::Players::TextArea do

  before(:each) do
    @block = Limelight::Block.new
    @block.add_controller(Limelight::Players::TextArea)
  end
  
  it "should get rid of the all painters and add a TextAreaPainter" do
    @block.panel.painters.size.should == 1
    @block.panel.painters.last.class.should == Java::limelight.ui.painting.TextAreaPainter
  end
  
  it "should clear event listeners on the panel" do
    @block.panel.mouse_listeners.length.should == 0
    @block.panel.key_listeners.length.should == 0
  end
  
  it "should have a JTextArea" do
    @block.panel.components[0].class.should == javax.swing.JTextArea
  end
  
  it "should use the TextArea for the text accessor" do
    @block.text = "blah"
    @block.panel.components[0].text.should == "blah"
    
    @block.panel.components[0].text = "harumph"
    @block.text.should == "harumph"
  end

end